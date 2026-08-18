package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaddleTransactionPaymentFailedHandler implements PaddleEventHandler {
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "transaction.payment_failed";
    }

    @Override
    public void handle(PaddleEvent event) {
        JsonNode subscriptionId = event.data().get("subscription_id");
        if (subscriptionId == null || !subscriptionId.isTextual()) {
            log.info("Ignoring Paddle payment failure without a subscription ID");
            return;
        }
        planSubscriptionService.notifyPaymentFailed(subscriptionId.textValue());
    }
}
