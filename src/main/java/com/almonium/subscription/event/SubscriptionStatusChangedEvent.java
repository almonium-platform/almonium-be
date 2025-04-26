package com.almonium.subscription.event;

import com.almonium.infra.messaging.DomainEvent;
import com.almonium.subscription.model.entity.PlanSubscription;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized("events.exchange::subscription.status.changed.v1")
public record SubscriptionStatusChangedEvent(
        UUID userId,
        String recipientEmail,
        String recipientUsername, // Needed by composer service
        String planName,
        PlanSubscription.Event subscriptionEvent,
        Instant occurredAt)
        implements DomainEvent {

    public SubscriptionStatusChangedEvent(
            UUID userId,
            String recipientEmail,
            String recipientUsername,
            String planName,
            PlanSubscription.Event subscriptionEvent) {
        this(userId, recipientEmail, recipientUsername, planName, subscriptionEvent, Instant.now());
    }
}
