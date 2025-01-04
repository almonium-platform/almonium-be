package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.model.dto.EmailContext;
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
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.PlanService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    PlanSubscriptionRepository planSubRepository;
    PlanRepository planRepository;
    UserRepository userRepository;
    PlanService planService;
    InsiderRepository insiderRepository;

    public String initiatePlanSubscribing(User user, long planId) {
        Plan plan = getAndValidatePlanEligibility(user, planId);
        setCustomerIdIfNeeded(user);
        plan = isInsider(user)
                ? planService.getInsiderPlan()
                : plan; // backdoor for insiders, they can't subscribe to regular plans

        return stripeApiService.createPaymentSession(user, plan);
    }

    public String initiateCustomerPortalAccess(User user) {
        if (user.getStripeCustomerId() == null) {
            throw new PlanSubscriptionException("User has no Stripe customer ID");
        }
        return stripeApiService.createBillingPortalSessionForUser(user);
    }

    // goal - downgrade user to the default plan
    public void downgradeMe(User user) {
        PlanSubscription activeSubscription = getActiveSub(user);
        if (isPlanDefault(activeSubscription.getPlan())) {
            throw new BadUserRequestActionException("User is already on the default plan");
        } else if (planService.isPlanInsider(activeSubscription.getPlan().getId())) {
            updatePlanSubStatusAndSave(activeSubscription, PlanSubscription.Status.INACTIVE);
            findAndActivateDefaultPlan(activeSubscription.getUser());
        } else {
            cancelSubImmediately(activeSubscription);
        }
    }

    // Global actions
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

    // on account deletion
    public void cleanUpPaidSubscriptionsIfAny(User user) {
        PlanSubscription activeSubscription = getActiveSub(user);
        if (activeSubscription.getStripeSubscriptionId() != null) {
            cancelSubImmediately(activeSubscription);
        }
    }

    // on user registration
    public void assignDefaultPlanToUser(User user) {
        createNewPlanSub(user, planService.getDefaultPlan(), null, Instant.now(), null);
    }

    // For StripeWebhookService
    public void assignInsiderPlanToCustomer(String customerId) {
        User user = getUserByStripeCustomerIdOrThrow(customerId);

        getInsiderPlanSub(user)
                .ifPresentOrElse(
                        insiderPlanSub -> {
                            deactivateCurrentSub(user);
                            activateLifetimePlan(insiderPlanSub);
                        },
                        () -> replaceCurrentPlanSubWithNewPremium(
                                user, planService.getInsiderPlan(), null, Instant.now(), null));
    }

    public void disableSubscriptionRenewal(String stripeSubscriptionId) {
        PlanSubscription planSubscription = getPlanSubFromStripeData(stripeSubscriptionId);
        updatePlanSubStatusAndSave(planSubscription, PlanSubscription.Status.ACTIVE_TILL_CYCLE_END);
        sendEmailForEvent(planSubscription.getUser(), planSubscription, PlanSubscription.Event.CANCELED);
    }

    public void renewSubscription(String stripeSubscriptionId) {
        PlanSubscription planSubscription = getPlanSubFromStripeData(stripeSubscriptionId);
        updatePlanSubStatusAndSave(planSubscription, PlanSubscription.Status.ACTIVE);
        sendEmailForEvent(planSubscription.getUser(), planSubscription, PlanSubscription.Event.RENEWED);
    }

    public void putSubscriptionOnHold(String stripeSubscriptionId) {
        PlanSubscription planSubscription = getPlanSubFromStripeData(stripeSubscriptionId);
        sendEmailForEvent(planSubscription.getUser(), planSubscription, PlanSubscription.Event.PAYMENT_FAILED);
    }

    public void replaceCurrentPlanSubWithNewPremium(
            String customerId, String stripePriceId, String stripeSubscriptionId, Instant startDate, Instant endDate) {
        Plan plan = getPlanByStripePriceIdOrThrow(stripePriceId);
        User user = getUserByStripeCustomerIdOrThrow(customerId);
        replaceCurrentPlanSubWithNewPremium(user, plan, stripeSubscriptionId, startDate, endDate);
    }

    private Plan getPlanByStripePriceIdOrThrow(String stripePriceId) {
        return planRepository
                .findByStripePriceId(stripePriceId)
                .orElseThrow(() -> new StripeIntegrationException("Plan not found with price ID: " + stripePriceId));
    }

    public void cancelSubscription(String stripeSubscriptionId) {
        PlanSubscription targetedPlanSub = getPlanSubFromStripeData(stripeSubscriptionId);
        PlanSubscription.Status status = targetedPlanSub.getStatus();

        if (status == PlanSubscription.Status.CANCELED) {
            log.info("Subscription {} is already canceled", targetedPlanSub.getId());
            return;
        }

        // main path: user cancelled the subscription some time ago, and now the billing cycle is ending
        if (status == PlanSubscription.Status.ACTIVE) {
            updatePlanSubStatusAndSave(targetedPlanSub, PlanSubscription.Status.CANCELED);
            sendEmailForEvent(targetedPlanSub.getUser(), targetedPlanSub, PlanSubscription.Event.ENDED);
            findAndActivateDefaultPlan(targetedPlanSub.getUser());
        }
    }

    private PlanSubscription getDefaultPlanSubOrThrow(User user) {
        return user.getPlanSubscriptions().stream()
                .filter(planSubscription -> isPlanDefault(planSubscription.getPlan()))
                .findFirst()
                .orElseThrow(() -> new PlanSubscriptionException("No default plan found for user " + user.getId()));
    }

    private Optional<PlanSubscription> getInsiderPlanSub(User user) {
        return user.getPlanSubscriptions().stream()
                .filter(planSubscription ->
                        planService.isPlanInsider(planSubscription.getPlan().getId()))
                .findFirst();
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

    private void cancelSubImmediately(PlanSubscription activeSubscription) {
        assertStripeSubIdIsPresent(activeSubscription);
        stripeApiService.cancelSubImmediately(activeSubscription.getStripeSubscriptionId());
    }

    private PlanSubscription getPlanSubFromStripeData(String stripeSubscriptionId) {
        return planSubRepository
                .findByStripeSubscriptionId(stripeSubscriptionId)
                .orElseThrow(() -> new StripeIntegrationException(
                        "Plan subscription not found with subscription ID: " + stripeSubscriptionId));
    }

    private Plan getAndValidatePlanEligibility(User user, long planId) {
        Plan targetPlan = planRepository
                .findById(planId)
                .orElseThrow(() -> new BadUserRequestActionException("Plan not found with ID: " + planId));

        // insider and free will be filtered out
        planService.getAvailableRecurringPremiumPlans().stream()
                .filter(planDto -> planDto.id() == planId)
                .findAny()
                .orElseThrow(() -> new BadUserRequestActionException("Plan is not available for subscription"));

        PlanSubscription activeSub = getActiveSub(user);
        Plan activePlan = activeSub.getPlan();

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
        Optional.ofNullable(user.getStripeCustomerId())
                .ifPresentOrElse(
                        customerId -> log.info("User {} has Stripe customer ID {}", user.getId(), customerId),
                        // if user has no Stripe customer ID, create one
                        () -> {
                            String customerId = stripeApiService.createCustomerIdForUser(user);
                            user.setStripeCustomerId(customerId);
                            userRepository.save(user);
                            log.info("Updated user {} with Stripe customer ID {}", user.getId(), customerId);
                        });
    }

    private void replaceCurrentPlanSubWithNewPremium(
            User user, Plan plan, String stripeSubscriptionId, Instant startDate, Instant endDate) {

        if (SetupStep.PLAN.equals(user.getSetupStep())) {
            user.setSetupStep(SetupStep.PLAN.nextStep());
            userRepository.save(user);
        }

        deactivateCurrentSub(user);
        createPremiumPlanSubAndNotify(user, plan, stripeSubscriptionId, startDate, endDate);
    }

    private void deactivateCurrentSub(User user) {
        PlanSubscription activeSubscription = getActiveSub(user);

        // lifetime plan is not canceled, it's just set to inactive
        PlanSubscription.Status status = activeSubscription.getPlan().getType() == Plan.Type.LIFETIME
                ? PlanSubscription.Status.INACTIVE
                : PlanSubscription.Status.CANCELED;
        updatePlanSubStatusAndSave(activeSubscription, status);
    }

    private User getUserByStripeCustomerIdOrThrow(String customerId) {
        return userRepository
                .findByStripeCustomerId(customerId)
                .orElseThrow(() -> new StripeIntegrationException("User not found with customer ID: " + customerId));
    }

    private void createPremiumPlanSubAndNotify(
            User user, Plan plan, String stripeSubscriptionId, Instant startDate, Instant endDate) {
        createNewPlanSub(user, plan, stripeSubscriptionId, startDate, endDate);
        sendEmailForEvent(user, getActiveSub(user), PlanSubscription.Event.CREATED);
    }

    private void assertStripeSubIdIsPresent(PlanSubscription planSubscription) {
        if (planSubscription.getStripeSubscriptionId() == null) {
            throw new PlanSubscriptionException("Subscription ID is null for subscription " + planSubscription.getId());
        }
    }

    private void createNewPlanSub(
            User user, Plan plan, String stripeSubscriptionId, Instant startDate, Instant endDate) {

        PlanSubscription planSubscription = PlanSubscription.builder()
                .user(user)
                .plan(plan)
                .status(PlanSubscription.Status.ACTIVE)
                .stripeSubscriptionId(stripeSubscriptionId)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        user.getPlanSubscriptions().add(planSubscription);
        planSubRepository.save(planSubscription);
        log.info("Assigned free plan to user {}", user.getId());
    }

    // if business logic requires to cancel subscription at the end of the billing period
    @SuppressWarnings("unused")
    private void scheduleCancellation(PlanSubscription activeSubscription) {
        assertStripeSubIdIsPresent(activeSubscription);
        stripeApiService.scheduleSubCancellation(activeSubscription.getStripeSubscriptionId());
        log.info(
                "Subscription cancellation scheduled for user {}",
                activeSubscription.getUser().getId());
    }

    private boolean isPlanDefault(Plan activePlan) {
        return planService.isPlanDefault(activePlan.getId());
    }

    private void sendEmailForEvent(User user, PlanSubscription planSubscription, PlanSubscription.Event planSubEvent) {
        var emailContext = new EmailContext<>(
                planSubEvent,
                Map.of(
                        SubscriptionEmailComposerService.PLAN_NAME,
                        planSubscription.getPlan().getName()));
        EmailDto emailDto = emailComposerService.composeEmail(user.getEmail(), emailContext);
        emailService.sendEmail(emailDto);
    }

    private void updatePlanSubStatusAndSave(PlanSubscription planSubscription, PlanSubscription.Status status) {
        planSubscription.setStatus(status);
        planSubRepository.save(planSubscription);
        log.info("Subscription {} set to {}", planSubscription.getId(), status);
    }

    private void findAndActivateDefaultPlan(User user) {
        PlanSubscription defaultPlanSub = getDefaultPlanSubOrThrow(user);
        activateLifetimePlan(defaultPlanSub);
    }

    private void activateLifetimePlan(PlanSubscription planSubscription) {
        planSubscription.setStartDate(Instant.now());
        planSubscription.setEndDate(null);
        updatePlanSubStatusAndSave(planSubscription, PlanSubscription.Status.ACTIVE);
    }
}
