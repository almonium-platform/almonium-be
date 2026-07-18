package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerDeletedHandler implements StripeEventHandler {
    private final StripeEventObjectExtractor extractor;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "customer.deleted";
    }

    @Override
    public void handle(Event event) {
        Customer customer = extractor.extract(event, Customer.class);
        planSubscriptionService.removeCustomer(customer.getId());
    }
}
