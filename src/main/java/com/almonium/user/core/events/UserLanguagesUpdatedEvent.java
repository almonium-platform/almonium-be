package com.almonium.user.core.events;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.properties.RabbitMQProperties;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized(RabbitMQProperties.EVENTS_EXCHANGE_NAME + "::user.languages.updated.v1")
public record UserLanguagesUpdatedEvent(UUID userId, List<Language> targetLanguages, Instant occurredAt)
        implements DomainEvent {

    public UserLanguagesUpdatedEvent(UUID userId, List<Language> targetLanguages) {
        this(userId, targetLanguages, Instant.now());
    }
}
