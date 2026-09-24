package com.almonium.user.core.events;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.modulith.events.Externalized;

/**
 * Carries what the consumers need after the row is gone: the account is deleted in the same transaction that publishes
 * this, so nothing downstream can look the user up again. That is why the address, the username and the plan travel
 * with the event rather than being fetched by the listener that sends the farewell mail, and why the ids of the books
 * they imported do too: the processor holds those files, and the rows that named them cascade away with the account.
 */
@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::user.deleted.v1")
public record UserDeletedEvent(
        UUID userId,
        Optional<String> paddleSubscriptionId,
        String email,
        String username,
        Optional<String> planName,
        List<UUID> bookImportIds,
        Instant occurredAt)
        implements DomainEvent {

    public UserDeletedEvent(
            UUID userId,
            @NonNull Optional<String> paddleSubscriptionId,
            String email,
            String username,
            @NonNull Optional<String> planName,
            @NonNull List<UUID> bookImportIds) {
        this(userId, paddleSubscriptionId, email, username, planName, List.copyOf(bookImportIds), Instant.now());
    }
}
