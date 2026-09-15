package com.almonium.infra.messaging.consumer;

import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.SubscriptionEmailComposerService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.subscription.event.SubscriptionStatusChangedEvent;
import com.almonium.subscription.model.entity.PlanSubscription;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionEmailListener {
    private final SubscriptionEmailComposerService emailComposerService;

    @RabbitListener(queues = "${rabbitmq.queue.subscription-status-changed.name}")
    public void handleSubscriptionStatusChanged(SubscriptionStatusChangedEvent event) {
        log.info(
                "Received subscription status changed event for email: {}, type: {}",
                event.recipientEmail(),
                event.subscriptionEvent());

        try {
            EmailContext<PlanSubscription.Event> emailContext =
                    new EmailContext<>(event.subscriptionEvent(), attributes(event));

            emailComposerService.sendEmail(event.recipientUsername(), event.recipientEmail(), emailContext);

            log.info("Successfully processed subscription email request for {}", event.recipientEmail());

        } catch (Exception e) {
            log.error(
                    "Failed processing subscription email request for {}. Error: {}",
                    event.recipientEmail(),
                    e.getMessage(),
                    e);
            throw new EventProcessingException("Subscription email processing failed for " + event.recipientEmail(), e);
        }
    }

    private static Map<String, String> attributes(SubscriptionStatusChangedEvent event) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(SubscriptionEmailComposerService.PLAN_NAME, event.planName());
        if (event.periodEndsAt() != null) {
            attributes.put(
                    SubscriptionEmailComposerService.PERIOD_ENDS_AT,
                    event.periodEndsAt().toString());
        }
        return attributes;
    }
}
