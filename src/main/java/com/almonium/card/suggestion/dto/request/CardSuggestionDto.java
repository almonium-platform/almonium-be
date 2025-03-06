package com.almonium.card.suggestion.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CardSuggestionDto(@NotNull UUID recipientId, @NotNull UUID cardId) {}
