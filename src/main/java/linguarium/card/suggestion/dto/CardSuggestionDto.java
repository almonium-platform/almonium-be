package linguarium.card.suggestion.dto;

import jakarta.validation.constraints.NotNull;

public record CardSuggestionDto(@NotNull Long recipientId, @NotNull Long cardId) {}
