package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CheckoutSessionCompletedHandler implements StripeEventHandler {
    private final StripeEventObjectExtractor extractor;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "checkout.session.completed";
    }

    @Override
    public void handle(Event event) {
        Session session = extractor.extract(event, Session.class);
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
}
