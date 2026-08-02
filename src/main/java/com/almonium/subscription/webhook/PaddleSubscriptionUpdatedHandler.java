package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.fasterxml.jackson.databind.JsonNode;
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
        JsonNode scheduledAction = event.data().at("/scheduled_change/action");
        planSubscriptionService.reconcileSubscription(
                eventData.requiredText(event.data(), "/id"),
                eventData.requiredText(event.data(), "/status"),
                scheduledAction.isTextual() && "cancel".equals(scheduledAction.textValue()),
                eventData.requiredInstant(event.data(), "/current_billing_period/starts_at"),
                eventData.requiredInstant(event.data(), "/current_billing_period/ends_at"));
    }
}
