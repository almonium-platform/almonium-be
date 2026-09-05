package com.almonium.card.deck.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record DeckCreationRequest(
        @NotBlank @Size(max = 60) String title, @NotNull Language language, List<UUID> wordIds) {}
