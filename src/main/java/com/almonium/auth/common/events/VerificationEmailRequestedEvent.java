package com.almonium.auth.common.events;

import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::auth.verification-email.requested.v1")
public record VerificationEmailRequestedEvent(
        UUID userId, String recipientEmail, String token, TokenType tokenType, Instant occurredAt)
        implements DomainEvent {

    public VerificationEmailRequestedEvent(UUID userId, String recipientEmail, String token, TokenType tokenType) {
        this(userId, recipientEmail, token, tokenType, Instant.now());
    }
}
