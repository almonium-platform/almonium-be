package com.almonium.user.core.events;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.modulith.events.Externalized;

@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::user.deleted.v1")
public record UserDeletedEvent(UUID userId, Optional<String> paddleSubscriptionId, Instant occurredAt)
        implements DomainEvent {

    public UserDeletedEvent(UUID userId, @NonNull Optional<String> paddleSubscriptionId) {
        this(userId, paddleSubscriptionId, Instant.now());
    }
}
