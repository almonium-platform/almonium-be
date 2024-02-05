package com.linguarium.card.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
public class CardUpdateDto {
    @NotNull
    Long id;

    String entry;
    TranslationDto[] translations;
    String notes;
    TagDto[] tags;
    ExampleDto[] examples;
    LocalDateTime created;
    LocalDateTime lastRepeat;
    Integer iteration;
    Long userId;
    Integer priority;
    int[] deletedTranslationsIds;
    int[] deletedExamplesIds;
    LocalDateTime updated;
    Boolean activeLearning;
    Boolean falseFriend;
    Boolean irregularPlural;
    Boolean irregularSpelling;
    String language;
}
