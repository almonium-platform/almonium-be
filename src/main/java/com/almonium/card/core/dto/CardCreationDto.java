package com.almonium.card.core.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CardCreationDto {
    @NotBlank
    String entry;

    @NotBlank
    String language;

    @NotEmpty
    TranslationDto[] translations;

    String notes;
    TagDto[] tags;
    ExampleDto[] examples;
    boolean activeLearning;
    boolean irregularPlural;
    boolean falseFriend;
    boolean irregularSpelling;
    boolean learnt;

    String createdAt;
    String updatedAt;
    Integer priority;
}
