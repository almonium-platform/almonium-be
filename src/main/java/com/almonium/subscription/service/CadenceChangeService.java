package com.almonium.subscription.service;

import static com.almonium.subscription.model.entity.enums.CadenceChangeOption.PRORATED_NOW;
import static com.almonium.subscription.model.entity.enums.CadenceChangeOption.REFUND_AND_SWITCH;
import static com.almonium.subscription.model.entity.enums.CadenceChangeOption.SCHEDULED;
import static com.almonium.subscription.model.entity.enums.ProrationBillingMode.DO_NOT_BILL;
import static com.almonium.subscription.model.entity.enums.ProrationBillingMode.FULL_IMMEDIATELY;
import static com.almonium.subscription.model.entity.enums.ProrationBillingMode.PRORATED_IMMEDIATELY;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.review.repository.ReviewSessionRepository;
import com.almonium.subscription.dto.response.AnnualNudgeDto;
import com.almonium.subscription.dto.response.CadenceChangeOptionDto;
import com.almonium.subscription.dto.response.CadenceChangePreviewDto;
import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.enums.CadenceChangeOption;
import com.almonium.subscription.model.entity.enums.ProrationBillingMode;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.subscription.service.PaddleApiService.CadenceChangePreview;
import com.almonium.subscription.service.PaddleApiService.PaidTransaction;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import jakarta.transaction.Transactional;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Moving between monthly and annual billing, in whichever direction is safe for the direction asked.
 *
 * <p>The two directions are not mirror images. Monthly to annual prorates immediately: the credit for the unused month
 * is smaller than the annual charge, so the difference settles on the card and the member is simply upgraded. Annual to
 * monthly cannot work that way. The credit for the unused months dwarfs the monthly charge, and Paddle puts the excess
 * on the customer's account balance rather than back on their card - a founder switching in month two would sit on
 * roughly seventy dollars they cannot see and did not ask for.
 *
 * <p>So the downgrade is scheduled for renewal, with nothing due and nothing refunded, unless the annual payment is
 * still inside the guarantee window. Then it can be undone properly: refund the payment, start monthly today, leave no
 * balance behind. Telling a six-day-old subscriber their switch lands in three hundred and fifty-nine days is the worst
 * version of this flow, and it reaches the most enthusiastic buyers first.
 *
 * <p>Scheduling is ours rather than Paddle's, which was not the original plan. Paddle refuses a next-billing-period
 * billing mode whenever the new price changes the billing interval - verified against the sandbox, which answers
 * {@code subscription_new_items_not_valid} - and accepts it only for a price on the same cycle. Its {@code
 * scheduled_change} covers cancel, pause and resume, never an item swap. A pending cadence change is therefore held
 * locally, told to Paddle nowhere, and applied as the period closes.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CadenceChangeService {
    /** The refund promise made on the pricing page. A switch inside it is a refund, not a schedule. */
    static final Duration GUARANTEE_WINDOW = Duration.ofDays(14);

    /** Use, not elapsed time. Eight finished sessions is a habit; thirty days is only a failure to cancel. */
    static final int SESSIONS_BEFORE_ANNUAL_OFFER = 8;

    PaddleApiService paddleApiService;
    PaddlePriceCatalog paddlePriceCatalog;
    PlanSubscriptionService planSubscriptionService;
    PlanSubscriptionRepository planSubRepository;
    PlanRepository planRepository;
    ReviewSessionRepository reviewSessionRepository;
    Clock clock;

    public CadenceChangePreviewDto preview(User user, Plan.Type target) {
        Change change = resolve(user, target);
        List<CadenceChangeOptionDto> options = optionsFor(change);
        CadenceChangePreview reference = change.previewOf(options.getFirst().kind());
        return new CadenceChangePreviewDto(
                change.currentType,
                target,
                reference.recurring().minorUnits(),
                change.founder,
                reference.recurring().currencyCode(),
                change.currentPeriodEndsAt,
                options);
    }

    public void apply(User user, Plan.Type target, CadenceChangeOption option) {
        Change change = resolve(user, target);
        // The client picks from what was offered; it does not get to name a billing mode. Honouring an unoffered
        // option is how an annual member talks their way into an invisible credit balance.
        boolean offered = optionsFor(change).stream().anyMatch(offer -> offer.kind() == option);
        if (!offered) {
            throw new BadUserRequestActionException("That billing change is not available for this subscription");
        }
        switch (option) {
            case PRORATED_NOW -> applyImmediately(change, PRORATED_IMMEDIATELY);
            case SCHEDULED -> schedule(change);
            case REFUND_AND_SWITCH -> refundAndSwitch(change);
        }
    }

    /**
     * Whether to offer the move to annual billing.
     *
     * <p>Tied to eight finished review sessions rather than to a thirty-day timer: the offer is worth making to
     * someone who has found the habit, and worth withholding from someone who merely has not cancelled yet. Only a
     * monthly subscription can take it, and only Paddle knows whether the switch is affordable, so the offer says
     * nothing about price - it opens the same confirmation any switch does.
     */
    public AnnualNudgeDto annualNudge(User user) {
        long completed = reviewSessionRepository.countByOwnerIdAndCompletedAtIsNotNull(user.getId());
        PlanSubscription subscription = planSubscriptionService.getActiveSub(user);
        boolean eligible = subscription.getPlan().getType() == Plan.Type.MONTHLY
                && subscription.getStatus() == PlanSubscription.Status.ACTIVE
                && subscription.getScheduledPlan() == null
                && completed >= SESSIONS_BEFORE_ANNUAL_OFFER;
        return new AnnualNudgeDto(eligible, completed, SESSIONS_BEFORE_ANNUAL_OFFER);
    }

    /**
     * Undoing a pending change is a local delete. Paddle was never told about it, nothing was billed, and the
     * subscription has been on its original cadence the whole time.
     */
    public void undo(User user) {
        PlanSubscription subscription = planSubscriptionService.getActiveSub(user);
        if (subscription.getScheduledPlan() == null) {
            throw new BadUserRequestActionException("There is no scheduled billing change to undo");
        }
        clearScheduledChange(subscription);
    }

    private List<CadenceChangeOptionDto> optionsFor(Change change) {
        return change.isUpgrade() ? upgradeOptions(change) : downgradeOptions(change);
    }

    private List<CadenceChangeOptionDto> upgradeOptions(Change change) {
        CadenceChangePreview preview = change.previewOf(PRORATED_NOW);
        return List.of(new CadenceChangeOptionDto(
                PRORATED_NOW,
                true,
                preview.dueNow().minorUnits(),
                creditOrNull(preview.credit().minorUnits()),
                null,
                clock.instant(),
                change.nextBilledAt(preview)));
    }

    private List<CadenceChangeOptionDto> downgradeOptions(Change change) {
        List<CadenceChangeOptionDto> options = new ArrayList<>();
        Optional<PaidTransaction> refundable = change.refundableTransaction();
        // The refund path leads when it exists: it is the only one that gives an enthusiastic new subscriber what they
        // asked for today rather than next year.
        refundable.ifPresent(transaction -> {
            CadenceChangePreview preview = change.previewOf(REFUND_AND_SWITCH);
            options.add(new CadenceChangeOptionDto(
                    REFUND_AND_SWITCH,
                    true,
                    preview.dueNow().minorUnits(),
                    null,
                    transaction.total().minorUnits(),
                    clock.instant(),
                    change.nextBilledAt(preview)));
        });
        // Nothing is due and nothing is deferred, so there is no immediate transaction to read. The preview is asked
        // only what the new cadence costs in this member's currency; the dates come from the subscription itself,
        // since a preview reports the dates a change made *today* would produce.
        change.previewOf(SCHEDULED);
        options.add(new CadenceChangeOptionDto(
                SCHEDULED,
                refundable.isEmpty(),
                0L,
                null,
                null,
                change.currentPeriodEndsAt,
                change.currentPeriodEndsAt));
        return options;
    }

    /**
     * Paddle states a proration credit as a negative amount, being money owed back. Screens print it with their own
     * minus sign beside a "Credit" label, so the magnitude is what travels. Zero is not a credit at all: a row reading
     * "Credit for this month $0.00" is noise on a confirmation screen.
     */
    private static Long creditOrNull(long minorUnits) {
        long magnitude = Math.abs(minorUnits);
        return magnitude > 0 ? magnitude : null;
    }

    private void applyImmediately(Change change, ProrationBillingMode mode) {
        paddleApiService.changeCadence(change.subscriptionId, change.targetPriceId, mode);
        clearScheduledChange(change.subscription);
    }

    /**
     * Scheduling touches Paddle not at all.
     *
     * <p>The obvious implementation was to hand Paddle the new price with next-billing-period behaviour and let it
     * defer the charge. Paddle rejects that outright for a change of billing interval - {@code
     * subscription_new_items_not_valid} - and only accepts deferred billing when the new price keeps the same cycle.
     * There is no scheduled item change to fall back on either; {@code scheduled_change} covers cancel, pause and
     * resume and nothing else.
     *
     * <p>So the intent is ours to hold. The annual subscription runs to its term untouched, which is exactly what the
     * screen promises: nothing due, no credit, no refund, no balance. {@link #applyDueChange} performs the switch as
     * the period closes, and until then the record here is the only thing that knows about it.
     */
    private void schedule(Change change) {
        PlanSubscription subscription = change.subscription;
        subscription.setScheduledPlan(change.targetPlan);
        subscription.setScheduledChangeAt(change.currentPeriodEndsAt);
        planSubRepository.save(subscription);
    }

    /**
     * Switches a subscription whose scheduled date has arrived.
     *
     * <p>Run shortly before the period closes rather than after: the change has to land before Paddle renews the
     * annual subscription for another year. Proration at that point covers whatever sliver of the period is left,
     * which is why this is billed immediately - there is nothing meaningful left to defer.
     */
    public void applyDueChange(PlanSubscription subscription) {
        if (subscription.getScheduledPlan() == null || subscription.getScheduledChangeAt() == null) {
            return;
        }
        String subscriptionId = requirePaddleSubscriptionId(subscription);
        boolean founder = paddlePriceCatalog.isFounderPrice(
                paddleApiService.getSubscription(subscriptionId).priceId());
        Plan targetPlan = subscription.getScheduledPlan();
        paddleApiService.changeCadence(
                subscriptionId, paddlePriceCatalog.priceIdFor(targetPlan.getType(), founder), PRORATED_IMMEDIATELY);
        subscription.setPlan(targetPlan);
        clearScheduledChange(subscription);
        log.info("Applied scheduled cadence change to {} for subscription {}", targetPlan.getType(), subscriptionId);
    }

    private void refundAndSwitch(Change change) {
        PaidTransaction transaction = change.refundableTransaction()
                .orElseThrow(() -> new BadUserRequestActionException("This payment is no longer refundable"));
        // The charge goes first. If it is declined nothing has moved, whereas a refund issued against a switch that
        // then fails would hand back the year and leave the member on annual anyway.
        applyImmediately(change, FULL_IMMEDIATELY);
        try {
            paddleApiService.refundTransactionInFull(transaction.id(), "Switched to monthly billing within guarantee");
        } catch (RuntimeException exception) {
            log.error(
                    "Switched subscription {} to monthly but failed to refund transaction {}",
                    change.subscriptionId,
                    transaction.id(),
                    exception);
            throw new PaddleIntegrationException(
                    "Your billing was switched to monthly but the refund could not be issued. "
                            + "Contact support and it will be refunded manually.",
                    exception);
        }
    }

    private Change resolve(User user, Plan.Type target) {
        if (target != Plan.Type.MONTHLY && target != Plan.Type.YEARLY) {
            throw new BadUserRequestActionException("Only monthly and annual billing can be switched between");
        }
        PlanSubscription subscription = planSubscriptionService.getActiveSub(user);
        Plan.Type currentType = subscription.getPlan().getType();
        if (currentType != Plan.Type.MONTHLY && currentType != Plan.Type.YEARLY) {
            throw new BadUserRequestActionException("This plan does not have a billing cadence to change");
        }
        if (currentType == target) {
            throw new BadUserRequestActionException(
                    "This subscription is already billed " + (target == Plan.Type.MONTHLY ? "monthly" : "annually"));
        }
        String subscriptionId = requirePaddleSubscriptionId(subscription);
        PaddleApiService.SubscriptionSnapshot snapshot = paddleApiService.getSubscription(subscriptionId);
        // A founder keeps their price across the change, which means targeting the founder price ID rather than the
        // public one. Miss this and the screen promises a locked price while the member is moved onto the list price.
        boolean founder = paddlePriceCatalog.isFounderPrice(snapshot.priceId());
        Plan targetPlan = planRepository
                .findByNameAndType(subscription.getPlan().getName(), target)
                .orElseThrow(() -> new PaddleIntegrationException("No plan exists for the requested billing cadence"));
        Instant currentPeriodEndsAt = snapshot.billingPeriodEndsAt()
                .orElseThrow(() ->
                        new PaddleIntegrationException("Subscription has no current billing period to change from"));
        return new Change(
                subscription,
                subscriptionId,
                currentType,
                targetPlan,
                paddlePriceCatalog.priceIdFor(target, founder),
                founder,
                currentPeriodEndsAt);
    }

    private void clearScheduledChange(PlanSubscription subscription) {
        subscription.setScheduledPlan(null);
        subscription.setScheduledChangeAt(null);
        planSubRepository.save(subscription);
    }

    private String requirePaddleSubscriptionId(PlanSubscription subscription) {
        if (subscription.getPaddleSubscriptionId() == null) {
            throw new BadUserRequestActionException("This membership is not billed through Paddle");
        }
        return subscription.getPaddleSubscriptionId();
    }

    /**
     * One requested change, holding what Paddle has already been asked. A confirmation screen wants the same figures
     * two or three times over - once to list the options, once to name the price, once to commit - and each of those is
     * a network call to a payment provider. It asks once per billing mode instead.
     */
    private final class Change {
        private final PlanSubscription subscription;
        private final String subscriptionId;
        private final Plan.Type currentType;
        private final Plan targetPlan;
        private final String targetPriceId;
        private final boolean founder;
        private final Instant currentPeriodEndsAt;
        private final Map<ProrationBillingMode, CadenceChangePreview> previews =
                new EnumMap<>(ProrationBillingMode.class);
        private Optional<PaidTransaction> refundable;

        private Change(
                PlanSubscription subscription,
                String subscriptionId,
                Plan.Type currentType,
                Plan targetPlan,
                String targetPriceId,
                boolean founder,
                Instant currentPeriodEndsAt) {
            this.subscription = subscription;
            this.subscriptionId = subscriptionId;
            this.currentType = currentType;
            this.targetPlan = targetPlan;
            this.targetPriceId = targetPriceId;
            this.founder = founder;
            this.currentPeriodEndsAt = currentPeriodEndsAt;
        }

        private boolean isUpgrade() {
            return currentType == Plan.Type.MONTHLY;
        }

        private CadenceChangePreview previewOf(CadenceChangeOption option) {
            return previews.computeIfAbsent(
                    modeFor(option),
                    mode -> paddleApiService.previewCadenceChange(subscriptionId, targetPriceId, mode));
        }

        private Instant nextBilledAt(CadenceChangePreview preview) {
            return preview.nextBilledAt().orElse(currentPeriodEndsAt);
        }

        private Optional<PaidTransaction> refundableTransaction() {
            if (refundable == null) {
                refundable = paddleApiService
                        .latestPaidTransaction(subscriptionId)
                        .filter(transaction -> transaction.total().minorUnits() > 0
                                && !transaction
                                        .billedAt()
                                        .plus(GUARANTEE_WINDOW)
                                        .isBefore(clock.instant()));
            }
            return refundable;
        }

        private ProrationBillingMode modeFor(CadenceChangeOption option) {
            return switch (option) {
                case PRORATED_NOW -> PRORATED_IMMEDIATELY;
                case SCHEDULED -> DO_NOT_BILL;
                case REFUND_AND_SWITCH -> FULL_IMMEDIATELY;
            };
        }
    }
}
