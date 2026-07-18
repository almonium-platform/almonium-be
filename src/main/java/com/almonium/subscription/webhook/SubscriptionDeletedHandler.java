package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionDeletedHandler implements StripeEventHandler {
    private final StripeEventObjectExtractor extractor;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "customer.subscription.deleted";
    }

    @Override
    public void handle(Event event) {
        Subscription subscription = extractor.extract(event, Subscription.class);
        planSubscriptionService.cancelSubscription(subscription.getId());
    }
}
