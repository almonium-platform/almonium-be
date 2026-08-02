package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.event.SubscriptionStatusChangedEvent;
import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.exception.PlanSubscriptionException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.PlanService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
            paddleApiService.cancelSubscriptionImmediately(activeSubscription.getPaddleSubscriptionId());
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
            Optional<Integer> foundingMemberSlot) {
        Plan plan = planRepository
                .findByNameAndType("PREMIUM", paddlePriceCatalog.planTypeFor(priceId))
                .orElseThrow(() -> new PaddleIntegrationException("Plan not found for Paddle price"));
        User user = getUserByPaddleCustomerIdOrThrow(customerId);
        Optional<PlanSubscription> existing = planSubRepository.findByPaddleSubscriptionId(subscriptionId);
        if (existing.isPresent()) {
            PlanSubscription subscription = existing.orElseThrow();
            subscription.setStartDate(startDate);
            subscription.setEndDate(endDate);
            if (subscription.getStatus() == PlanSubscription.Status.ACTIVE_TILL_CYCLE_END) {
                subscription.setStatus(PlanSubscription.Status.ACTIVE);
            }
            planSubRepository.save(subscription);
        } else {
            replaceCurrentPlanSubWithNewPremium(user, plan, subscriptionId, startDate, endDate);
        }
        foundingMemberSlot.ifPresent(
                slot -> foundingMemberService.confirm(slot, user.getId(), transactionId, subscriptionId));
    }

    public void reconcileSubscription(
            String subscriptionId, String status, boolean cancellationScheduled, Instant startDate, Instant endDate) {
        PlanSubscription subscription = getPlanSubFromPaddleData(subscriptionId);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);

        if ("canceled".equals(status)) {
            cancelSubscription(subscriptionId);
            return;
        }
        if ("active".equals(status) && cancellationScheduled) {
            if (subscription.getStatus() != PlanSubscription.Status.ACTIVE_TILL_CYCLE_END) {
                updatePlanSubStatusAndSave(subscription, PlanSubscription.Status.ACTIVE_TILL_CYCLE_END);
                sendEmailForEvent(subscription.getUser(), subscription, PlanSubscription.Event.CANCELED);
            } else {
                planSubRepository.save(subscription);
            }
            return;
        }
        if ("active".equals(status) && subscription.getStatus() == PlanSubscription.Status.ACTIVE_TILL_CYCLE_END) {
            updatePlanSubStatusAndSave(subscription, PlanSubscription.Status.ACTIVE);
            sendEmailForEvent(subscription.getUser(), subscription, PlanSubscription.Event.RENEWED);
            return;
        }
        planSubRepository.save(subscription);
    }

    public void putSubscriptionOnHold(String subscriptionId) {
        PlanSubscription planSubscription = getPlanSubFromPaddleData(subscriptionId);
        sendEmailForEvent(planSubscription.getUser(), planSubscription, PlanSubscription.Event.PAYMENT_FAILED);
    }

    public void cancelSubscription(String subscriptionId) {
        PlanSubscription targetedPlanSub = getPlanSubFromPaddleData(subscriptionId);
        if (targetedPlanSub.getStatus() == PlanSubscription.Status.CANCELED) {
            log.info("Subscription {} is already canceled", targetedPlanSub.getId());
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
            User user, Plan plan, String subscriptionId, Instant startDate, Instant endDate) {
        if (SetupStep.PLAN.equals(user.getSetupStep())) {
            user.setSetupStep(SetupStep.PLAN.nextStep());
            userRepository.save(user);
        }
        deactivateCurrentSub(user);
        createNewPlanSub(user, plan, subscriptionId, startDate, endDate);
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
        PlanSubscription planSubscription = PlanSubscription.builder()
                .user(user)
                .plan(plan)
                .status(PlanSubscription.Status.ACTIVE)
                .paddleSubscriptionId(paddleSubscriptionId)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        user.getPlanSubscriptions().add(planSubscription);
        planSubRepository.save(planSubscription);
        log.info("Assigned plan {} to user {}", plan.getName(), user.getId());
    }

    private PlanSubscription getPlanSubFromPaddleData(String subscriptionId) {
        return planSubRepository
                .findByPaddleSubscriptionId(subscriptionId)
                .orElseThrow(
                        () -> new PaddleIntegrationException("Plan subscription not found for Paddle subscription"));
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
                event));
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
    }
}
