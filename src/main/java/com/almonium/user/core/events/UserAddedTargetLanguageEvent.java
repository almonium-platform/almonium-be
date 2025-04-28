package com.almonium.user.core.events;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::user.target-language.added.v1")
public record UserAddedTargetLanguageEvent(UUID userId, Language language, Instant occurredAt) implements DomainEvent {

    public UserAddedTargetLanguageEvent(UUID userId, Language language) {
        this(userId, language, Instant.now());
    }
}
