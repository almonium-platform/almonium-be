package com.almonium.card.deck.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DeckDto(
        UUID id,
        String title,
        Language language,
        String shareId,
        boolean shareEnabled,
        List<UUID> wordIds,
        Instant createdAt,
        Instant updatedAt) {}
