package com.almonium.subscription.webhook;

import com.stripe.model.Customer;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerCreatedHandler implements StripeEventHandler {
    private final StripeEventObjectExtractor extractor;

    @Override
    public String eventType() {
        return "customer.created";
    }

    @Override
    public void handle(Event event) {
        Customer customer = extractor.extract(event, Customer.class);
        log.info(
                "For customer with email {} and name {} Stripe assigned ID {}",
                customer.getEmail(),
                customer.getName(),
                customer.getId());
    }
}
