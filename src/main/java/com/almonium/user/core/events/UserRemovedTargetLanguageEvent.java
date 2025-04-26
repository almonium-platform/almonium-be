package com.almonium.user.core.events;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.messaging.DomainEvent;
import java.time.Instant;
import java.util.UUID;
import org.springframework.modulith.events.Externalized;

@Externalized("events.exchange::user.target-language.removed.v1")
public record UserRemovedTargetLanguageEvent(UUID userId, Language language, Instant occurredAt)
        implements DomainEvent {

    public UserRemovedTargetLanguageEvent(UUID userId, Language language) {
        this(userId, language, Instant.now());
    }
}
