package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaddleSubscriptionUpdatedHandler implements PaddleEventHandler {
    private final PaddleEventData eventData;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "subscription.updated";
    }

    @Override
    public void handle(PaddleEvent event) {
        String status = eventData.requiredText(event.data(), "/status");
        planSubscriptionService.reconcileSubscription(
                eventData.requiredText(event.data(), "/id"),
                eventData.requiredText(event.data(), "/items/0/price/id"),
                status,
                eventData.scheduledChangeAction(event.data()),
                eventData.optionalInstant(event.data(), "/current_billing_period/starts_at"),
                eventData.optionalInstant(event.data(), "/current_billing_period/ends_at"),
                event.occurredAt());
    }
}
