package com.almonium.subscription.event;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::user.deleted.paddle.v1")
public record PaddleUserCleanupRequestedEvent(UUID userId, Optional<String> subscriptionId, Instant occurredAt)
        implements DomainEvent {
    public PaddleUserCleanupRequestedEvent(UUID userId, Optional<String> subscriptionId) {
        this(userId, subscriptionId, Instant.now());
    }
}
