package com.almonium.subscription.webhook;

import com.almonium.subscription.exception.PaddleIntegrationException;
import com.almonium.subscription.service.PlanSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

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
        if (!"subscription_recurring".equals(event.data().path("origin").stringValue(null))) {
            return;
        }
        JsonNode subscriptionId = event.data().get("subscription_id");
        if (subscriptionId == null
                || !subscriptionId.isString()
                || subscriptionId.stringValue().isBlank()) {
            throw new PaddleIntegrationException("Recurring Paddle transaction is missing /subscription_id");
        }
        planSubscriptionService.notifyRenewed(subscriptionId.stringValue());
    }
}
