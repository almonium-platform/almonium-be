package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaddleSubscriptionCreatedHandler implements PaddleEventHandler {
    private final PaddleEventData eventData;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "subscription.created";
    }

    @Override
    public void handle(PaddleEvent event) {
        planSubscriptionService.syncSubscription(
                eventData.requiredText(event.data(), "/customer_id"),
                eventData.requiredText(event.data(), "/items/0/price/id"),
                eventData.requiredText(event.data(), "/id"),
                eventData.requiredText(event.data(), "/transaction_id"),
                eventData.requiredInstant(event.data(), "/current_billing_period/starts_at"),
                eventData.requiredInstant(event.data(), "/current_billing_period/ends_at"),
                eventData.foundingMemberSlot(event.data()));
    }
}
