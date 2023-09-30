package com.linguarium.suggestion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardAcceptanceDto {

    @NotNull
    Long senderId;

    @NotNull
    Long cardId;
}
