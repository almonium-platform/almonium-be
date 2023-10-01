package com.linguarium.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CardCreationDto {

    @NotBlank
    String entry;
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
    @NotNull
    String language;
    String created;
    String updated;
    Integer priority;
}
