package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionCreatedHandler implements StripeEventHandler {
    private final StripeEventObjectExtractor extractor;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "customer.subscription.created";
    }

    @Override
    public void handle(Event event) {
        Subscription subscription = extractor.extract(event, Subscription.class);
        String stripePriceId =
                subscription.getItems().getData().get(0).getPrice().getId();
        planSubscriptionService.replaceCurrentPlanSubWithNewPremium(
                subscription.getCustomer(),
                stripePriceId,
                subscription.getId(),
                Instant.ofEpochSecond(subscription.getCurrentPeriodStart()),
                Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()));
    }
}
