package com.almonium.user.relationship.event;

import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

/**
 * A friendship that has just become mutual. The private chat belonging to it is created off this event rather than by
 * whichever client pressed Accept: the requester is usually not online at all, and the accepter may have answered from
 * the notification bell, which knows nothing about Stream.
 */
@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::friendship.accepted.v1")
public record FriendshipAcceptedEvent(UUID relationshipId, UUID accepterId, UUID counterpartId, Instant occurredAt)
        implements DomainEvent {

    public FriendshipAcceptedEvent(UUID relationshipId, UUID accepterId, UUID counterpartId) {
        this(relationshipId, accepterId, counterpartId, Instant.now());
    }
}
