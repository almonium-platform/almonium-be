package com.almonium.user.core.events;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::user.deleted.v1")
public record UserDeletedEvent(UUID userId, Instant occurredAt) implements DomainEvent {

    public UserDeletedEvent(UUID userId) {
        this(userId, Instant.now());
    }
}
