package com.almonium.user.core.events;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::username.updated.v1")
public record UsernameUpdatedEvent(UUID userId, String username, Instant occurredAt) implements DomainEvent {

    public UsernameUpdatedEvent(UUID userId, String username) {
        this(userId, username, Instant.now());
    }
}
