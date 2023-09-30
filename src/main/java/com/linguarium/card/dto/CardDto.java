package com.linguarium.card.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CardDto {
    Long id;
    String publicId;
    Long userId;
    @NotBlank
    String entry;
    @NotNull
    LanguageDto language;
    @NotEmpty
    TranslationDto[] translations;
    String notes;
    TagDto[] tags;
    ExampleDto[] examples;
    LocalDateTime created;
    LocalDateTime updated;
    int iteration;
    int priority;
    boolean activeLearning;
    boolean irregularPlural;
    boolean irregularSpelling;
    boolean falseFriend;
}
