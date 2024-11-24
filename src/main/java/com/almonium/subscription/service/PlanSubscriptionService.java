package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.EmailService;
import com.almonium.infra.email.service.SubscriptionEmailComposerService;
import com.almonium.subscription.exception.PlanSubscriptionException;
import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.InsiderRepository;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.impl.PlanService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PlanSubscriptionService {
    EmailService emailService;
    SubscriptionEmailComposerService emailComposerService;
    StripeApiService stripeApiService;
    PlanSubscriptionRepository planSubscriptionRepository;
    PlanRepository planRepository;
    UserRepository userRepository;
    PlanService planService;
    InsiderRepository insiderRepository;

    // User initiated actions
    public String initiatePlanSubscribing(User user, long planId) {
        Plan plan = planRepository
                .findById(planId)
                .orElseThrow(() -> new BadUserRequestActionException("Plan not found with ID: " + planId));

        if (getActivePlan(user).equals(plan)) {
            throw new BadUserRequestActionException(
                    "User already has an active subscription to this plan " + plan.getName());
        }

        // We can't subscribe to a free plan. We can return to a free plan if we cancel the subscription.
        assertPlanIsPremium(plan);
        if (isInsider(user)) {
            assignPremiumPlanToUserAndNotify(user, plan);
        }

        return stripeApiService.createPaymentSession(user, plan);
    }

    public void assignPremiumPlanToUserAndNotify(User user, Plan plan) {
        assignPlanToUser(user, plan);
        sendEmailForEvent(user, getActiveSubscription(user), PlanSubscription.Event.SUBSCRIPTION_CREATED);
    }

    public void initiateSubscriptionCancellation(User user) {
        PlanSubscription activeSubscription = getActiveSubscription(user);
        assertPlanIsPremium(activeSubscription.getPlan());
        updatePlanSubStatus(activeSubscription, PlanSubscription.Status.PENDING_CANCELLATION);
        updatePlanSubStatus(getOrThrowDefaultPlan(user), PlanSubscription.Status.ACTIVE);
        scheduleCancellation(activeSubscription);
    }

    // Global actions
    public PlanSubscription getActiveSubscription(User user) {
        return planSubscriptionRepository
                .findByUserAndStatus(user, PlanSubscription.Status.ACTIVE)
                .orElseThrow(
                        () -> new PlanSubscriptionException("No active subscription found for user " + user.getId()));
    }

    public void cleanUpPaidSubscriptionsIfAny(User user) {
        PlanSubscription activeSubscription = getActiveSubscription(user);
        if (isPlanDefault(activeSubscription.getPlan())) {
            log.info("User {} is not a paying customer. No subscription to cancel.", user.getId());
            return;
        }
        assertStripeSubscriptionIdIsPresent(activeSubscription);
        stripeApiService.cancelSubscriptionImmediately(activeSubscription.getStripeSubscriptionId());
    }

    public Plan getActivePlan(User user) {
        return getActiveSubscription(user).getPlan();
    }

    public void assignFreePlanToUser(User user) {
        Plan freePlan = planService.getDefaultPlan();
        assignPlanToUser(user, freePlan);
    }

    // For StripeApiService to call
    public void updateSubscriptionStatusToOnHold(PlanSubscription planSubscription) {
        updatePlanSubStatus(planSubscription, PlanSubscription.Status.ON_HOLD);
        sendEmailForEvent(
                planSubscription.getUser(), planSubscription, PlanSubscription.Event.SUBSCRIPTION_PAYMENT_FAILED);
    }

    public void createSubscription(String email, String customerId, String stripePriceId, String stripeSubscriptionId) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new StripeIntegrationException("User not found with email: " + email));

        if (user.getStripeCustomerId() == null) {
            // if it's user's first subscription
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
            log.info("Updated user {} with Stripe customer ID {}", user.getId(), customerId);
        }

        Plan plan = planRepository
                .findByStripePriceId(stripePriceId)
                .orElseThrow(() -> new StripeIntegrationException("Plan not found with price ID: " + stripePriceId));

        PlanSubscription activeSubscription = getActiveSubscription(user);
        if (isPlanDefault(activeSubscription.getPlan())) {
            updatePlanSubStatus(activeSubscription, PlanSubscription.Status.INACTIVE);
        } else {
            updatePlanSubStatus(activeSubscription, PlanSubscription.Status.PENDING_CANCELLATION);
            scheduleCancellation(activeSubscription);
        }

        assignPremiumPlanToUserAndNotify(user, plan);
    }

    public void cancelSubscription(PlanSubscription planSubscription) {
        if (planSubscription.getStatus() != PlanSubscription.Status.PENDING_CANCELLATION) {
            throw new PlanSubscriptionException("Subscription is not pending cancellation");
        }
        updatePlanSubStatus(planSubscription, PlanSubscription.Status.CANCELED);
        Plan currentPlan = getActivePlan(planSubscription.getUser());
        Plan toBeCanceledPlan = planSubscription.getPlan();
        boolean hasAnotherPremiumSubscription =
                !isPlanDefault(currentPlan) && !Objects.equals(toBeCanceledPlan.getId(), currentPlan.getId());

        boolean isDowngrade = !hasAnotherPremiumSubscription;
        if (isDowngrade) {
            sendEmailForEvent(
                    planSubscription.getUser(), planSubscription, PlanSubscription.Event.SUBSCRIPTION_CANCELED);
        }
    }

    // unexpected - we normally don't delete stripe customers
    public void removeCustomer(String customerId) {
        userRepository
                .findByStripeCustomerId(customerId)
                .ifPresentOrElse(
                        user -> {
                            user.setStripeCustomerId(null);
                            userRepository.save(user);
                            log.warn("Removed Stripe customer ID from user {}", user.getId());
                        },
                        () -> log.warn("No user found with Stripe customer ID {}", customerId));
    }

    private boolean isInsider(User user) {
        return insiderRepository.existsById(user.getId());
    }

    private void assertStripeSubscriptionIdIsPresent(PlanSubscription planSubscription) {
        if (planSubscription.getStripeSubscriptionId() == null) {
            throw new PlanSubscriptionException("Subscription ID is null for subscription " + planSubscription.getId());
        }
    }

    private PlanSubscription getOrThrowDefaultPlan(User user) {
        return user.getPlanSubscriptions().stream()
                .filter(planSubscription -> isPlanDefault(planSubscription.getPlan()))
                .findFirst()
                .orElseThrow(() -> new PlanSubscriptionException("No default plan found for user " + user.getId()));
    }

    private void assignPlanToUser(User user, Plan plan) {
        PlanSubscription planSubscription = PlanSubscription.builder()
                .user(user)
                .plan(plan)
                .status(PlanSubscription.Status.ACTIVE)
                .startDate(Instant.now())
                .build();

        user.getPlanSubscriptions().add(planSubscription);
        planSubscriptionRepository.save(planSubscription);
        log.info("Assigned free plan to user {}", user.getId());
    }

    private void scheduleCancellation(PlanSubscription activeSubscription) {
        assertStripeSubscriptionIdIsPresent(activeSubscription);
        stripeApiService.scheduleSubscriptionCancellation(activeSubscription.getStripeSubscriptionId());
        activeSubscription.setAutoRenewal(false);
        planSubscriptionRepository.save(activeSubscription);
        log.info(
                "Subscription cancellation scheduled for user {}",
                activeSubscription.getUser().getId());
    }

    private void assertPlanIsPremium(Plan activePlan) {
        if (isPlanDefault(activePlan)) {
            throw new BadUserRequestActionException("This action is not allowed for free plans.");
        }
    }

    private boolean isPlanDefault(Plan activePlan) {
        return planService.isPlanDefault(activePlan.getId());
    }

    private void sendEmailForEvent(
            User user, PlanSubscription planSubscription, PlanSubscription.Event subscriptionEvent) {
        EmailDto emailDto = emailComposerService.composeEmail(
                user.getEmail(), subscriptionEvent, planSubscription.getPlan().getName());
        emailService.sendEmail(emailDto);
    }

    private void updatePlanSubStatus(PlanSubscription planSubscription, PlanSubscription.Status status) {
        planSubscription.setStatus(status);
        planSubscriptionRepository.save(planSubscription);
        log.info("Subscription {} set to {}", planSubscription.getId(), status);
    }
}
