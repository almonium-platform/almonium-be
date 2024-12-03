package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.user.core.model.entity.User;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class StripeApiService {

    @Value("${stripe.checkout.success-url}")
    String successUrl;

    @Value("${stripe.checkout.cancel-url}")
    String cancelUrl;

    @Value("${stripe.return-url}")
    String returnUrl;

    public String createBillingPortalSessionForUser(User user) {
        try {
            com.stripe.param.billingportal.SessionCreateParams params =
                    com.stripe.param.billingportal.SessionCreateParams.builder()
                            .setCustomer(user.getStripeCustomerId())
                            .setReturnUrl(returnUrl)
                            .build();

            return com.stripe.model.billingportal.Session.create(params).getUrl();
        } catch (StripeException e) {
            log.error("Failed to create session for user with ID {}", user.getId());
            throw new StripeIntegrationException("Failed to create session: " + e.getMessage(), e);
        }
    }

    // if business logic requires to cancel subscription immediately
    public void cancelSubImmediately(String stripeSubscriptionId) {
        try {
            Subscription.retrieve(stripeSubscriptionId).cancel();
            log.info("Canceled subscription with ID {}", stripeSubscriptionId);
        } catch (StripeException e) {
            log.error("Failed to cancel subscription with ID {}", stripeSubscriptionId);
            throw new StripeIntegrationException("Failed to cancel subscription: " + e.getMessage(), e);
        }
    }

    public void scheduleSubCancellation(String stripeSubscriptionId) {
        try {
            SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                    .setCancelAtPeriodEnd(true)
                    .build();

            Subscription.retrieve(stripeSubscriptionId).update(params);
            log.info("Scheduled cancellation for subscription ID {}", stripeSubscriptionId);
        } catch (StripeException e) {
            log.error("Failed to schedule cancellation for subscription ID {}", stripeSubscriptionId);
            throw new StripeIntegrationException("Failed to schedule cancellation: " + e.getMessage(), e);
        }
    }

    public String createPaymentSession(User user, Plan plan) {
        try {
            return Session.create(buildCheckoutSessionForUserAndPlan(user, plan))
                    .getUrl();
        } catch (StripeException e) {
            log.error("Stripe exception occurred while creating payment session: {}", e.getMessage());
            throw new StripeIntegrationException("Failed to create payment session", e);
        }
    }

    public String createCustomerIdForUser(User user) {
        try {
            Customer stripeCustomer = Customer.create(CustomerCreateParams.builder()
                    .setEmail(user.getEmail())
                    .putMetadata("userId", user.getId().toString())
                    .build());

            return Optional.ofNullable(stripeCustomer.getId())
                    .orElseThrow(() -> new StripeIntegrationException("User ID is null"));
        } catch (StripeException e) {
            throw new StripeIntegrationException("Failed to create customer for user with ID: " + user.getId(), e);
        }
    }

    private SessionCreateParams buildCheckoutSessionForUserAndPlan(User user, Plan plan) {
        var mode = plan.getType().equals(Plan.Type.LIFETIME)
                ? SessionCreateParams.Mode.PAYMENT
                : SessionCreateParams.Mode.SUBSCRIPTION;

        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .setCustomer(user.getStripeCustomerId())
                .setMode(mode)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(plan.getStripePriceId())
                        .setQuantity(1L)
                        .build())
                .setClientReferenceId(user.getId().toString());

        return params.build();
    }
}
