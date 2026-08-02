package com.almonium.subscription.webhook;

import com.almonium.subscription.service.PlanSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaddleSubscriptionCanceledHandler implements PaddleEventHandler {
    private final PaddleEventData eventData;
    private final PlanSubscriptionService planSubscriptionService;

    @Override
    public String eventType() {
        return "subscription.canceled";
    }

    @Override
    public void handle(PaddleEvent event) {
        planSubscriptionService.cancelSubscription(eventData.requiredText(event.data(), "/id"));
    }
}
