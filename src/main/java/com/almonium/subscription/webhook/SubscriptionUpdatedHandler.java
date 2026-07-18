package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubscriptionUpdatedHandler implements StripeEventHandler {
    private final StripeEventObjectExtractor extractor;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "customer.subscription.updated";
    }

    @Override
    public void handle(Event event) {
        Subscription subscription = extractor.extract(event, Subscription.class);
        String subscriptionId = subscription.getId();
        log.info("Subscription updated: ID {}", subscriptionId);

        Map<String, Object> previousAttributes = event.getData().getPreviousAttributes();
        if (previousAttributes == null || previousAttributes.isEmpty()) {
            log.info("No previous attributes found");
            return;
        }

        previousAttributes.forEach((attribute, value) -> log.info("Previous {}: {}", attribute, value));
        if (previousAttributes.containsKey("cancel_at_period_end")) {
            boolean wasCancelAtPeriodEnd = (boolean) previousAttributes.get("cancel_at_period_end");
            boolean isCancelAtPeriodEnd = subscription.getCancelAtPeriodEnd();

            if (wasCancelAtPeriodEnd && !isCancelAtPeriodEnd) {
                log.info("Subscription is reactivated, ID: {}", subscriptionId);
                planSubscriptionService.renewSubscription(subscriptionId);
            }
            if (!wasCancelAtPeriodEnd && isCancelAtPeriodEnd) {
                log.info("Subscription will be canceled at the end of its billing period, ID: {}", subscriptionId);
                planSubscriptionService.disableSubscriptionRenewal(subscriptionId);
            }
        }

        log.info("Current subscription status: {}", subscription.getStatus());
        log.info("Current default payment method: {}", subscription.getDefaultPaymentMethod());
    }
}
