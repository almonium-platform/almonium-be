package com.almonium.subscription.service;

import static com.almonium.subscription.model.entity.enums.CadenceChangeOption.PRORATED_NOW;
import static com.almonium.subscription.model.entity.enums.CadenceChangeOption.REFUND_AND_SWITCH;
import static com.almonium.subscription.model.entity.enums.CadenceChangeOption.SCHEDULED;
import static com.almonium.subscription.model.entity.enums.ProrationBillingMode.DO_NOT_BILL;
import static com.almonium.subscription.model.entity.enums.ProrationBillingMode.FULL_IMMEDIATELY;
import static com.almonium.subscription.model.entity.enums.ProrationBillingMode.FULL_NEXT_BILLING_PERIOD;
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

    /** Undoing a pending change costs nothing and refunds nothing, because nothing has been billed yet. */
    public void undo(User user) {
        PlanSubscription subscription = planSubscriptionService.getActiveSub(user);
        if (subscription.getScheduledPlan() == null) {
            throw new BadUserRequestActionException("There is no scheduled billing change to undo");
        }
        String subscriptionId = requirePaddleSubscriptionId(subscription);
        boolean founder = paddlePriceCatalog.isFounderPrice(
                paddleApiService.getSubscription(subscriptionId).priceId());
        paddleApiService.changeCadence(
                subscriptionId,
                paddlePriceCatalog.priceIdFor(subscription.getPlan().getType(), founder),
                DO_NOT_BILL);
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
                positiveOrNull(preview.credit().minorUnits()),
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
        CadenceChangePreview scheduled = change.previewOf(SCHEDULED);
        Instant effectiveAt = change.scheduledEffectiveAt(scheduled);
        options.add(new CadenceChangeOptionDto(
                SCHEDULED,
                refundable.isEmpty(),
                scheduled.dueNow().minorUnits(),
                null,
                null,
                effectiveAt,
                effectiveAt));
        return options;
    }

    /** Zero is not a credit. A row reading "Credit for this month  $0.00" is noise on a confirmation screen. */
    private static Long positiveOrNull(long minorUnits) {
        return minorUnits > 0 ? minorUnits : null;
    }

    private void applyImmediately(Change change, ProrationBillingMode mode) {
        paddleApiService.changeCadence(change.subscriptionId, change.targetPriceId, mode);
        clearScheduledChange(change.subscription);
    }

    private void schedule(Change change) {
        Instant effectiveAt = change.scheduledEffectiveAt(change.previewOf(SCHEDULED));
        paddleApiService.changeCadence(change.subscriptionId, change.targetPriceId, FULL_NEXT_BILLING_PERIOD);
        PlanSubscription subscription = change.subscription;
        subscription.setScheduledPlan(change.targetPlan);
        subscription.setScheduledChangeAt(effectiveAt);
        planSubRepository.save(subscription);
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

        private Instant scheduledEffectiveAt(CadenceChangePreview preview) {
            return preview.nextBilledAt().orElse(currentPeriodEndsAt);
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
                case SCHEDULED -> FULL_NEXT_BILLING_PERIOD;
                case REFUND_AND_SWITCH -> FULL_IMMEDIATELY;
            };
        }
    }
}
