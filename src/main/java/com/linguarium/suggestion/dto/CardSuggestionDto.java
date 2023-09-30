package com.linguarium.suggestion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardSuggestionDto {

    @NotNull
    Long recipientId;

    @NotNull
    Long cardId;
}
