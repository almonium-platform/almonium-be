package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.event.EntitlementChangedEvent;
import com.almonium.subscription.event.SubscriptionStatusChangedEvent;
import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.exception.PlanSubscriptionException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.record.UserEntitlement;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.PlanService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PlanSubscriptionService {
    PaddleApiService paddleApiService;
    PaddlePriceCatalog paddlePriceCatalog;
    FoundingMemberService foundingMemberService;
    PlanService planService;

    PlanSubscriptionRepository planSubRepository;
    PlanRepository planRepository;
    UserRepository userRepository;

    ApplicationEventPublisher eventPublisher;

    public String initiatePlanSubscribing(User user, long planId, boolean founderRequested) {
        Plan plan = getAndValidatePlanEligibility(user, planId);
        setCustomerIdIfNeeded(user);
        Optional<Integer> foundingMemberSlot = founderRequested
                ? Optional.of(foundingMemberService
                        .reserveForCheckout(user)
                        .orElseThrow(() ->
                                new BadUserRequestActionException("The founding-member offer is no longer available")))
                : Optional.empty();
        try {
            PaddleApiService.CheckoutTransaction transaction =
                    paddleApiService.createPaymentTransaction(user, plan, foundingMemberSlot);
            foundingMemberSlot.ifPresent(
                    slot -> foundingMemberService.attachTransaction(slot, user.getId(), transaction.id()));
            return transaction.checkoutUrl();
        } catch (RuntimeException exception) {
            foundingMemberSlot.ifPresent(
                    slot -> foundingMemberService.releaseReservationAfterCheckoutFailure(slot, user.getId()));
            throw exception;
        }
    }

    public String initiateCustomerPortalAccess(User user) {
        if (user.getPaddleCustomerId() == null) {
            throw new PlanSubscriptionException("User has no Paddle customer ID");
        }
        return paddleApiService.createCustomerPortalSessionForUser(user);
    }

    public void downgradeMe(User user) {
        PlanSubscription activeSubscription = getActiveSub(user);
        if (isPlanDefault(activeSubscription.getPlan())) {
            throw new BadUserRequestActionException("User is already on the default plan");
        }
        if (activeSubscription.getPlan().getType() == Plan.Type.LIFETIME) {
            updatePlanSubStatusAndSave(activeSubscription, PlanSubscription.Status.INACTIVE);
            findAndActivateDefaultPlan(activeSubscription.getUser());
        } else {
            assertPaddleSubscriptionIdIsPresent(activeSubscription);
            // Cancelling at the end of the cycle rather than on the spot. The member has paid through the period, the
            // confirmation step tells them they keep everything until it ends, and taking access away the moment they
            // confirm would make that a lie. Paddle reports the pending cancellation as a scheduled change, which
            // reconciliation already turns into ACTIVE_TILL_CYCLE_END.
            paddleApiService.scheduleSubscriptionCancellation(activeSubscription.getPaddleSubscriptionId());
        }
    }

    public PlanSubscription getActiveSub(User user) {
        return planSubRepository
                .findByUserAndStatusIn(
                        user, List.of(PlanSubscription.Status.ACTIVE, PlanSubscription.Status.ACTIVE_TILL_CYCLE_END))
                .orElseThrow(
                        () -> new PlanSubscriptionException("No active subscription found for user " + user.getId()));
    }

    public Plan getActivePlan(User user) {
        return getActiveSub(user).getPlan();
    }

    /** Plan-derived entitlements for a list of users, in one query. */
    public List<UserEntitlement> getActiveEntitlements(Collection<UUID> userIds) {
        return planSubRepository.findActiveEntitlementsByUserIds(userIds);
    }

    public Optional<String> getPaidSubscriptionIdToCancel(User user) {
        return Optional.ofNullable(getActiveSub(user).getPaddleSubscriptionId());
    }

    public void assignDefaultPlanToUser(User user) {
        createNewPlanSub(user, planService.getDefaultPlan(), null, Instant.now(), null);
    }

    public void syncSubscription(
            String customerId,
            String priceId,
            String subscriptionId,
            String transactionId,
            Instant startDate,
            Instant endDate,
            Optional<Integer> foundingMemberSlot,
            Instant occurredAt) {
        Plan plan = getPlanForPaddlePrice(priceId);
        User user = getUserByPaddleCustomerIdOrThrow(customerId);
        Optional<PlanSubscription> existing = planSubRepository.findForUpdateByPaddleSubscriptionId(subscriptionId);
        if (existing.isPresent()) {
            PlanSubscription subscription = existing.orElseThrow();
            if (isNewerPaddleLifecycleEvent(subscription, occurredAt)) {
                subscription.setPlan(plan);
                subscription.setStartDate(startDate);
                subscription.setEndDate(endDate);
                subscription.setLatestPaddleEventOccurredAt(occurredAt);
                planSubRepository.save(subscription);
            }
        } else {
            replaceCurrentPlanSubWithNewPremium(user, plan, subscriptionId, startDate, endDate, occurredAt);
        }
        foundingMemberSlot.ifPresent(
                slot -> foundingMemberService.confirm(slot, user.getId(), transactionId, subscriptionId));
    }

    public void reconcileSubscription(
            String subscriptionId,
            String priceId,
            String status,
            Optional<String> scheduledChangeAction,
            Optional<Instant> startDate,
            Optional<Instant> endDate,
            Instant occurredAt) {
        PlanSubscription subscription = getPlanSubFromPaddleDataForUpdate(subscriptionId);
        if (!isNewerPaddleLifecycleEvent(subscription, occurredAt)) {
            return;
        }
        PlanSubscription.Status previousStatus = subscription.getStatus();
        applyPaddlePriceToPlan(subscription, priceId, occurredAt);
        if (!List.of("canceled", "paused").contains(status) && (startDate.isEmpty() || endDate.isEmpty())) {
            throw new PaddleIntegrationException("Active Paddle subscription is missing its current billing period");
        }
        startDate.ifPresent(subscription::setStartDate);
        endDate.ifPresent(subscription::setEndDate);
        subscription.setLatestPaddleEventOccurredAt(occurredAt);

        if ("canceled".equals(status)) {
            cancelSubscription(subscription);
            return;
        }
        if ("paused".equals(status)) {
            pauseSubscription(subscription);
            return;
        }
        if (!List.of("active", "trialing", "past_due").contains(status)) {
            throw new PaddleIntegrationException("Unsupported Paddle subscription status: " + status);
        }
        if (scheduledChangeAction.filter("cancel"::equals).isPresent()) {
            if (subscription.getStatus() != PlanSubscription.Status.ACTIVE_TILL_CYCLE_END) {
                updatePlanSubStatusAndSave(subscription, PlanSubscription.Status.ACTIVE_TILL_CYCLE_END);
                sendEmailForEvent(subscription.getUser(), subscription, PlanSubscription.Event.CANCELED);
            } else {
                planSubRepository.save(subscription);
            }
            return;
        }
        if (previousStatus == PlanSubscription.Status.PAUSED) {
            deactivateCurrentSub(subscription.getUser());
            updatePlanSubStatusAndSave(subscription, PlanSubscription.Status.ACTIVE);
            eventPublisher.publishEvent(
                    new EntitlementChangedEvent(subscription.getUser().getId()));
            return;
        }
        if (previousStatus == PlanSubscription.Status.ACTIVE_TILL_CYCLE_END) {
            updatePlanSubStatusAndSave(subscription, PlanSubscription.Status.ACTIVE);
            sendEmailForEvent(subscription.getUser(), subscription, PlanSubscription.Event.REACTIVATED);
            return;
        }
        planSubRepository.save(subscription);
    }

    public void notifyPaymentFailed(String subscriptionId) {
        PlanSubscription planSubscription = getPlanSubFromPaddleDataForUpdate(subscriptionId);
        if (planSubscription.getStatus() == PlanSubscription.Status.CANCELED) {
            return;
        }
        sendEmailForEvent(planSubscription.getUser(), planSubscription, PlanSubscription.Event.PAYMENT_FAILED);
    }

    public void notifyRenewed(String subscriptionId) {
        PlanSubscription planSubscription = getPlanSubFromPaddleDataForUpdate(subscriptionId);
        if (planSubscription.getStatus() == PlanSubscription.Status.CANCELED) {
            return;
        }
        sendEmailForEvent(planSubscription.getUser(), planSubscription, PlanSubscription.Event.RENEWED);
    }

    public void cancelSubscription(String subscriptionId, Instant occurredAt) {
        PlanSubscription targetedPlanSub = getPlanSubFromPaddleDataForUpdate(subscriptionId);
        if (!isNewerPaddleLifecycleEvent(targetedPlanSub, occurredAt)) {
            return;
        }
        targetedPlanSub.setLatestPaddleEventOccurredAt(occurredAt);
        cancelSubscription(targetedPlanSub);
    }

    private void cancelSubscription(PlanSubscription targetedPlanSub) {
        if (targetedPlanSub.getStatus() == PlanSubscription.Status.CANCELED) {
            log.info("Subscription {} is already canceled", targetedPlanSub.getId());
            planSubRepository.save(targetedPlanSub);
            return;
        }
        updatePlanSubStatusAndSave(targetedPlanSub, PlanSubscription.Status.CANCELED);
        sendEmailForEvent(targetedPlanSub.getUser(), targetedPlanSub, PlanSubscription.Event.ENDED);
        findAndActivateDefaultPlan(targetedPlanSub.getUser());
    }

    private PlanSubscription getDefaultPlanSubOrThrow(User user) {
        return user.getPlanSubscriptions().stream()
                .filter(planSubscription -> isPlanDefault(planSubscription.getPlan()))
                .findFirst()
                .orElseThrow(() -> new PlanSubscriptionException("No default plan found for user " + user.getId()));
    }

    private Plan getAndValidatePlanEligibility(User user, long planId) {
        Plan targetPlan = planRepository
                .findById(planId)
                .orElseThrow(() -> new BadUserRequestActionException("Plan not found with ID: " + planId));

        planService.getAvailableRecurringPremiumPlans().stream()
                .filter(planDto -> planDto.id() == planId)
                .findAny()
                .orElseThrow(() -> new BadUserRequestActionException("Plan is not available for subscription"));

        Plan activePlan = getActiveSub(user).getPlan();
        if (activePlan.equals(targetPlan)) {
            throw new BadUserRequestActionException(
                    "User already has an active subscription to this plan " + targetPlan.getName());
        }
        if (!isPlanDefault(activePlan)) {
            throw new BadUserRequestActionException("Cancel the current subscription before subscribing to a new one.");
        }
        return targetPlan;
    }

    private void setCustomerIdIfNeeded(User user) {
        if (user.getPaddleCustomerId() != null) {
            return;
        }
        String customerId = paddleApiService.createCustomerIdForUser(user);
        user.setPaddleCustomerId(customerId);
        userRepository.save(user);
        log.info("Updated user {} with Paddle customer ID", user.getId());
    }

    private void replaceCurrentPlanSubWithNewPremium(
            User user, Plan plan, String subscriptionId, Instant startDate, Instant endDate, Instant occurredAt) {
        deactivateCurrentSub(user);
        createNewPlanSub(user, plan, subscriptionId, startDate, endDate, occurredAt);
        eventPublisher.publishEvent(new EntitlementChangedEvent(user.getId()));
        sendEmailForEvent(user, getActiveSub(user), PlanSubscription.Event.CREATED);
    }

    private void deactivateCurrentSub(User user) {
        PlanSubscription activeSubscription = getActiveSub(user);
        PlanSubscription.Status status = activeSubscription.getPlan().getType() == Plan.Type.LIFETIME
                ? PlanSubscription.Status.INACTIVE
                : PlanSubscription.Status.CANCELED;
        updatePlanSubStatusAndSave(activeSubscription, status);
    }

    private User getUserByPaddleCustomerIdOrThrow(String customerId) {
        return userRepository
                .findByPaddleCustomerId(customerId)
                .orElseThrow(() -> new PaddleIntegrationException("User not found for Paddle customer"));
    }

    private void assertPaddleSubscriptionIdIsPresent(PlanSubscription planSubscription) {
        if (planSubscription.getPaddleSubscriptionId() == null) {
            throw new PlanSubscriptionException("Subscription ID is null for subscription " + planSubscription.getId());
        }
    }

    private void createNewPlanSub(
            User user, Plan plan, String paddleSubscriptionId, Instant startDate, Instant endDate) {
        createNewPlanSub(user, plan, paddleSubscriptionId, startDate, endDate, null);
    }

    private void createNewPlanSub(
            User user,
            Plan plan,
            String paddleSubscriptionId,
            Instant startDate,
            Instant endDate,
            Instant latestPaddleEventOccurredAt) {
        PlanSubscription planSubscription = PlanSubscription.builder()
                .user(user)
                .plan(plan)
                .status(PlanSubscription.Status.ACTIVE)
                .paddleSubscriptionId(paddleSubscriptionId)
                .startDate(startDate)
                .endDate(endDate)
                .latestPaddleEventOccurredAt(latestPaddleEventOccurredAt)
                .build();
        user.getPlanSubscriptions().add(planSubscription);
        planSubRepository.save(planSubscription);
        log.info("Assigned plan {} to user {}", plan.getName(), user.getId());
    }

    private PlanSubscription getPlanSubFromPaddleDataForUpdate(String subscriptionId) {
        return planSubRepository
                .findForUpdateByPaddleSubscriptionId(subscriptionId)
                .orElseThrow(
                        () -> new PaddleIntegrationException("Plan subscription not found for Paddle subscription"));
    }

    /**
     * A scheduled cadence change reads as already made. Paddle swaps the subscription item the moment the change is
     * requested and defers only the billing, so an annual member who schedules monthly is reported to us as monthly
     * while they are still eleven months from their next bill. Until the change lands, the plan they paid for is the
     * plan they keep; once it does, the price becomes the truth again and the pending record goes.
     */
    private void applyPaddlePriceToPlan(PlanSubscription subscription, String priceId, Instant occurredAt) {
        Instant scheduledChangeAt = subscription.getScheduledChangeAt();
        if (scheduledChangeAt != null && occurredAt.isBefore(scheduledChangeAt)) {
            return;
        }
        subscription.setPlan(getPlanForPaddlePrice(priceId));
        subscription.setScheduledPlan(null);
        subscription.setScheduledChangeAt(null);
    }

    private Plan getPlanForPaddlePrice(String priceId) {
        return planRepository
                .findByNameAndType("PREMIUM", paddlePriceCatalog.planTypeFor(priceId))
                .orElseThrow(() -> new PaddleIntegrationException("Plan not found for Paddle price"));
    }

    private boolean isNewerPaddleLifecycleEvent(PlanSubscription subscription, Instant occurredAt) {
        Instant latest = subscription.getLatestPaddleEventOccurredAt();
        if (latest == null || occurredAt.isAfter(latest)) {
            return true;
        }
        log.info(
                "Ignoring stale Paddle lifecycle event for subscription {}; occurred at {}, latest is {}",
                subscription.getPaddleSubscriptionId(),
                occurredAt,
                latest);
        return false;
    }

    private void pauseSubscription(PlanSubscription subscription) {
        if (subscription.getStatus() == PlanSubscription.Status.PAUSED) {
            planSubRepository.save(subscription);
            return;
        }
        updatePlanSubStatusAndSave(subscription, PlanSubscription.Status.PAUSED);
        findAndActivateDefaultPlan(subscription.getUser());
    }

    private boolean isPlanDefault(Plan activePlan) {
        return planService.isPlanDefault(activePlan.getId());
    }

    private void sendEmailForEvent(User user, PlanSubscription planSubscription, PlanSubscription.Event event) {
        eventPublisher.publishEvent(new SubscriptionStatusChangedEvent(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                planSubscription.getPlan().getName(),
                event,
                planSubscription.getEndDate()));
    }

    private void updatePlanSubStatusAndSave(PlanSubscription planSubscription, PlanSubscription.Status status) {
        planSubscription.setStatus(status);
        planSubRepository.save(planSubscription);
        log.info("Subscription {} set to {}", planSubscription.getId(), status);
    }

    private void findAndActivateDefaultPlan(User user) {
        PlanSubscription defaultPlanSub = getDefaultPlanSubOrThrow(user);
        defaultPlanSub.setStartDate(Instant.now());
        defaultPlanSub.setEndDate(null);
        updatePlanSubStatusAndSave(defaultPlanSub, PlanSubscription.Status.ACTIVE);
        eventPublisher.publishEvent(new EntitlementChangedEvent(user.getId()));
    }
}
