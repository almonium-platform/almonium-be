package com.almonium.user.core.events;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::user.profile.updated.v1")
public record UserProfileUpdatedEvent(UUID userId, Instant occurredAt) implements DomainEvent {

    public UserProfileUpdatedEvent(UUID userId) {
        this(userId, Instant.now());
    }
}
