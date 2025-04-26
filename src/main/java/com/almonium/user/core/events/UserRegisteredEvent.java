package com.almonium.user.core.events;

import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized("events.exchange::user.stream.setup.v1")
public record UserRegisteredEvent(UUID userId, String username, String email, Instant occurredAt)
        implements DomainEvent {

    public UserRegisteredEvent(UUID userId, String username, String email) {
        this(userId, username, email, Instant.now());
    }
}
