package com.almonium.user.core.events;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.modulith.events.Externalized;

/**
 * Carries what the consumers need after the row is gone: the account is deleted in the same transaction that publishes
 * this, so nothing downstream can look the user up again. That is why the address, the username and the plan travel
 * with the event rather than being fetched by the listener that sends the farewell mail.
 */
@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::user.deleted.v1")
public record UserDeletedEvent(
        UUID userId,
        Optional<String> paddleSubscriptionId,
        String email,
        String username,
        Optional<String> planName,
        Instant occurredAt)
        implements DomainEvent {

    public UserDeletedEvent(
            UUID userId,
            @NonNull Optional<String> paddleSubscriptionId,
            String email,
            String username,
            @NonNull Optional<String> planName) {
        this(userId, paddleSubscriptionId, email, username, planName, Instant.now());
    }
}
