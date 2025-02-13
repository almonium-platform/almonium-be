package com.almonium.card.suggestion.dto.request;

import jakarta.validation.constraints.Positive;

public record CardSuggestionDto(@Positive Long recipientId, @Positive Long cardId) {}
