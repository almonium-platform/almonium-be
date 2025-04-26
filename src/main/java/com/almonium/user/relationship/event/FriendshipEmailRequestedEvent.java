package com.almonium.user.relationship.event;

import com.almonium.infra.messaging.DomainEvent;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized("events.exchange::friendship.email.requested.v1")
public record FriendshipEmailRequestedEvent(
        UUID recipientUserId,
        String recipientEmail,
        String recipientUsername,
        String counterpartUsername,
        FriendshipEvent friendshipEvent,
        Instant occurredAt)
        implements DomainEvent {

    public FriendshipEmailRequestedEvent(
            UUID recipientUserId,
            String recipientEmail,
            String recipientUsername,
            String counterpartUsername,
            FriendshipEvent friendshipEvent) {
        this(recipientUserId, recipientEmail, recipientUsername, counterpartUsername, friendshipEvent, Instant.now());
    }
}
