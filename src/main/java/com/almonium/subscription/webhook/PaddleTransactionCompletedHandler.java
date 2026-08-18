package com.almonium.subscription.webhook;

import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaddleTransactionCompletedHandler implements PaddleEventHandler {
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "transaction.completed";
    }

    @Override
    public void handle(PaddleEvent event) {
        if (!"subscription_recurring".equals(event.data().path("origin").textValue())) {
            return;
        }
        JsonNode subscriptionId = event.data().get("subscription_id");
        if (subscriptionId == null
                || !subscriptionId.isTextual()
                || subscriptionId.textValue().isBlank()) {
            throw new PaddleIntegrationException("Recurring Paddle transaction is missing /subscription_id");
        }
        planSubscriptionService.notifyRenewed(subscriptionId.textValue());
    }
}
