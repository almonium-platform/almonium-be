package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.user.core.model.entity.User;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StripeApiService {

    @Value("${stripe.checkout.success-url}")
    @NonFinal
    String successUrl;

    @Value("${stripe.checkout.cancel-url}")
    @NonFinal
    String cancelUrl;

    // if business logic requires to cancel subscription immediately
    public void cancelSubscriptionImmediately(String stripeSubscriptionId) {
        try {
            Subscription subscription = Subscription.retrieve(stripeSubscriptionId);
            subscription.cancel();
            log.info("Canceled subscription with ID {}", subscription.getId());
        } catch (StripeException e) {
            log.error("Failed to cancel subscription with ID {}", stripeSubscriptionId);
            throw new StripeIntegrationException("Failed to cancel subscription: " + e.getMessage(), e);
        }
    }

    public void scheduleSubscriptionCancellation(String stripeSubscriptionId) {
        try {
            Subscription subscription = Subscription.retrieve(stripeSubscriptionId);
            SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                    .setCancelAtPeriodEnd(true)
                    .build();
            subscription.update(params);
            log.info("Scheduled cancellation for subscription ID {}", stripeSubscriptionId);
        } catch (StripeException e) {
            log.error("Failed to schedule cancellation for subscription ID {}", stripeSubscriptionId);
            throw new StripeIntegrationException("Failed to schedule cancellation: " + e.getMessage(), e);
        }
    }

    public Customer getCustomerById(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            throw new StripeIntegrationException("Failed to retrieve customer with ID: " + customerId, e);
        }
    }

    public String createPaymentSession(User user, Plan plan) {
        try {
            SessionCreateParams params = buildSessionForUserAndPlan(user, plan);
            return Session.create(params).getUrl();
        } catch (StripeException e) {
            log.error("Stripe exception occurred while creating payment session: {}", e.getMessage());
            throw new StripeIntegrationException("Failed to create payment session", e);
        }
    }

    private SessionCreateParams buildSessionForUserAndPlan(User user, Plan plan) {
        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(plan.getStripePriceId())
                        .setQuantity(1L)
                        .build())
                .setClientReferenceId(user.getId().toString());

        if (user.getStripeCustomerId() != null) {
            params.setCustomer(user.getStripeCustomerId());
        } else {
            params.setCustomerEmail(user.getEmail());
        }
        return params.build();
    }
}
