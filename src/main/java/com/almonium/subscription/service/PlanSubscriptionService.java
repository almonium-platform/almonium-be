package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.EmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.subscription.exception.PlanSubscriptionException;
import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PlanSubscriptionService {
    EmailService emailService;
    EmailComposerService emailComposerService;
    StripeApiService stripeApiService;
    PlanSubscriptionRepository planSubscriptionRepository;
    PlanRepository planRepository;
    UserRepository userRepository;

    public String subscribeToPlan(User user, long planId) {
        Plan plan = planRepository
                .findById(planId)
                .orElseThrow(() -> new StripeIntegrationException("Plan not found: " + planId));

        Optional<PlanSubscription> activeSubscriptionOptional = findActiveSubscription(user);

        if (activeSubscriptionOptional.isPresent()) {
            PlanSubscription currentSubscription = activeSubscriptionOptional.get();
            if (currentSubscription.getPlan().equals(plan)) {
                throw new PlanSubscriptionException(
                        "User already has an active subscription to this plan " + plan.getName());
            }
            stripeApiService.cancelSubscription(currentSubscription.getStripeSubscriptionId());
            cancelPlanSubscription(currentSubscription);
        }

        return stripeApiService.createPaymentSession(user, plan);
    }

    public void removeCustomer(String customerId) {
        User user = userRepository
                .findByStripeCustomerId(customerId)
                .orElseThrow(() -> new StripeIntegrationException("User not found with customer ID: " + customerId));

        user.setStripeCustomerId(null);
        userRepository.save(user);
        log.info("Removed Stripe customer ID from user {}", user.getId());
    }

    public void createSubscription(String email, String customerId, String stripePriceId, String stripeSubscriptionId) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new StripeIntegrationException("User not found with email: " + email));

        if (user.getStripeCustomerId() == null) {
            user.setStripeCustomerId(customerId);
            userRepository.save(user);
            log.info("Updated user {} with Stripe customer ID {}", user.getId(), customerId);
        }

        Plan plan = planRepository
                .findByStripePriceId(stripePriceId)
                .orElseThrow(() -> new StripeIntegrationException("Plan not found with price ID: " + stripePriceId));

        PlanSubscription newPlanSubscription = PlanSubscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(stripeSubscriptionId)
                .status(PlanSubscription.Status.ACTIVE)
                .startDate(LocalDateTime.now())
                .build();

        planSubscriptionRepository.save(newPlanSubscription);
        log.info("Subscription created for user {} with subscription ID {}", user.getId(), stripeSubscriptionId);

        EmailDto emailDto = emailComposerService.composeSubscriptionEmail(
                user.getEmail(),
                PlanSubscription.Event.SUBSCRIPTION_CREATED,
                newPlanSubscription.getPlan().getName());
        emailService.sendEmail(emailDto);
    }

    public void updateSubscriptionStatusToOnHold(PlanSubscription planSubscription) {
        PlanSubscription.Status status = PlanSubscription.Status.ON_HOLD;
        planSubscription.setStatus(status);
        planSubscriptionRepository.save(planSubscription);
        logSubscriptionStatusChange(planSubscription, status);

        User user = planSubscription.getUser();
        EmailDto emailDto = emailComposerService.composeSubscriptionEmail(
                user.getEmail(),
                PlanSubscription.Event.PAYMENT_FAILED,
                planSubscription.getPlan().getName());
        emailService.sendEmail(emailDto);
    }

    public void cancelPlanSubscription(PlanSubscription planSubscription) {
        User user = planSubscription.getUser();
        EmailDto emailDto = emailComposerService.composeSubscriptionEmail(
                user.getEmail(),
                PlanSubscription.Event.SUBSCRIPTION_CANCELED,
                planSubscription.getPlan().getName());
        emailService.sendEmail(emailDto);

        PlanSubscription.Status status = PlanSubscription.Status.CANCELED;
        planSubscription.setStatus(status);
        logSubscriptionStatusChange(planSubscription, status);
        planSubscriptionRepository.save(planSubscription);
    }

    public void cancelSubscription(User user) {
        PlanSubscription activeSubscription = findActiveSubscription(user)
                .orElseThrow(() -> new PlanSubscriptionException("No active subscription found for user " + user.getId()));
        stripeApiService.cancelSubscription(activeSubscription.getStripeSubscriptionId());
        cancelPlanSubscription(activeSubscription);
    }

    public Optional<PlanSubscription> findActiveSubscription(User user) {
        return planSubscriptionRepository.findByUserAndStatus(user, PlanSubscription.Status.ACTIVE);
    }

    private void logSubscriptionStatusChange(PlanSubscription planSubscription, PlanSubscription.Status status) {
        log.info("Subscription {} set to {}", planSubscription.getId(), status);
    }
}
