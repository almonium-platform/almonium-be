package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.exception.StripeIntegrationException;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.StripeEventLog;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.subscription.repository.StripeEventLogRepository;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StripeWebhookService {
    StripeEventLogRepository stripeEventLogRepository;
    PlanSubscriptionService planSubscriptionService;
    PlanSubscriptionRepository planSubscriptionRepository;
    StripeApiService stripeApiService;

    private static final List<String> EXPECTED_EVENT_TYPES = List.of(
            "payment_intent.created",
            "customer.updated",
            "invoice.created",
            "invoice.finalized",
            "charge.succeeded",
            "payment_method.attached",
            "payment_intent.succeeded",
            "invoice.updated",
            "invoice.paid",
            "invoice.payment_succeeded",
            "entitlements.active_entitlement_summary.updated");

    @Transactional
    public void handleWebhook(Event event) {
        if (stripeEventLogRepository.existsById(event.getId())) {
            log.info("Event {} already processed, skipping", event.getId());
            return;
        }

        stripeEventLogRepository.save(new StripeEventLog(
                event.getId(), event.getType(), Timestamp.from(Instant.ofEpochSecond(event.getCreated()))));
        log.info("Registered event: {}", event.getId());

        switch (event.getType()) {
            case "customer.created" -> handleCustomerCreated(event);
            case "customer.subscription.created" -> handleSubscriptionCreated(event);
            case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);
            case "invoice.payment_failed" -> handleInvoicePaymentFailed(event);
            case "customer.subscription.updated" -> handleSubscriptionUpdated(event);
            case "customer.subscription.deleted" -> handleSubscriptionDeleted(event);
            case "customer.deleted" -> handleCustomerDeleted(event);
            default -> handleOtherEvents(event.getType());
        }
    }

    private void handleCustomerDeleted(Event event) {
        Customer customer = getCustomerFromStripeEvent(event);
        planSubscriptionService.removeCustomer(customer.getId());
    }

    private void handleSubscriptionCreated(Event event) {
        Subscription subscription = getSubscriptionFromStripeEvent(event);

        String customerId = subscription.getCustomer();
        String stripePriceId =
                subscription.getItems().getData().get(0).getPrice().getId();

        Customer customer = stripeApiService.getCustomerById(customerId);
        planSubscriptionService.createSubscription(
                customer.getEmail(), customerId, stripePriceId, subscription.getId());
    }

    private void handleInvoicePaymentFailed(Event event) {
        Invoice invoice = getInvoiceFromStripeEvent(event);

        String stripeSubscriptionId = invoice.getSubscription();
        planSubscriptionService.updateSubscriptionStatusToOnHold(
                getPlanSubscriptionFromStripeData(stripeSubscriptionId));
    }

    private void handleSubscriptionDeleted(Event event) {
        Subscription subscription = getSubscriptionFromStripeEvent(event);
        PlanSubscription planSubscription = getPlanSubscriptionFromStripeData(subscription.getId());
        planSubscriptionService.cancelSubscription(planSubscription);
    }

    // logging methods

    private void handleOtherEvents(String type) {
        log.info(
                EXPECTED_EVENT_TYPES.contains(type)
                        ? "Expected but unhandled event type: {}"
                        : "Unexpected event type: {}",
                type);
    }

    private void handleSubscriptionUpdated(Event event) {
        Subscription subscription = getSubscriptionFromStripeEvent(event);

        String stripeSubscriptionId = subscription.getId();
        log.info("Subscription updated: ID {}", stripeSubscriptionId);

        Map<String, Object> previousAttributes = event.getData().getPreviousAttributes();
        if (previousAttributes != null && !previousAttributes.isEmpty()) {
            previousAttributes.forEach((attribute, value) -> log.info("Previous {}: {}", attribute, value));
        }

        log.info("Current subscription status: {}", subscription.getStatus());
        log.info("Current default payment method: {}", subscription.getDefaultPaymentMethod());
    }

    private void handleCustomerCreated(Event event) {
        Customer customer = getCustomerFromStripeEvent(event);

        String customerId = customer.getId();
        String email = customer.getEmail();
        String name = customer.getName();

        log.info("For customer with email {} and name {} Stripe assigned ID {}", email, name, customerId);
    }

    private void handleCheckoutSessionCompleted(Event event) {
        Session session = getSessionFromStripeEvent(event);

        log.info(
                "Checkout session completed: ID {}, Customer ID: {}, Subscription ID: {}, Amount Total: {}",
                session.getId(),
                session.getCustomer(),
                session.getSubscription(),
                session.getAmountTotal());

        Session.CustomerDetails customerDetails = session.getCustomerDetails();
        if (customerDetails != null) {
            log.info("Customer Email: {}, Name: {}", customerDetails.getEmail(), customerDetails.getName());
        }
    }

    private PlanSubscription getPlanSubscriptionFromStripeData(String stripeSubscriptionId) {
        return planSubscriptionRepository
                .findByStripeSubscriptionId(stripeSubscriptionId)
                .orElseThrow(() -> new StripeIntegrationException(
                        "Plan subscription not found with subscription ID: " + stripeSubscriptionId));
    }

    private Customer getCustomerFromStripeEvent(Event event) {
        return (Customer) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new StripeIntegrationException("Customer object is null"));
    }

    private static Subscription getSubscriptionFromStripeEvent(Event event) {
        return (Subscription) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new StripeIntegrationException("Subscription object is null"));
    }

    private Session getSessionFromStripeEvent(Event event) {
        return (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new StripeIntegrationException("Checkout session object is null"));
    }

    private Invoice getInvoiceFromStripeEvent(Event event) {
        return (Invoice) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new StripeIntegrationException("Invoice object is null"));
    }
}
