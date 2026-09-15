package com.almonium.subscription.event;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import com.almonium.subscription.model.entity.PlanSubscription;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

/**
 * @param periodEndsAt end of the current paid period as last synced from Paddle, or null when the subscription has
 *     none. The receipt prints it as the renewal date, the cancellation as the last day of access.
 */
@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::subscription.status.changed.v1")
public record SubscriptionStatusChangedEvent(
        UUID userId,
        String recipientEmail,
        String recipientUsername, // Needed by composer service
        String planName,
        PlanSubscription.Event subscriptionEvent,
        Instant periodEndsAt,
        Instant occurredAt)
        implements DomainEvent {

    public SubscriptionStatusChangedEvent(
            UUID userId,
            String recipientEmail,
            String recipientUsername,
            String planName,
            PlanSubscription.Event subscriptionEvent,
            Instant periodEndsAt) {
        this(userId, recipientEmail, recipientUsername, planName, subscriptionEvent, periodEndsAt, Instant.now());
    }
}
