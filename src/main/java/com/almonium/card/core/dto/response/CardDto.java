package com.almonium.card.core.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.dto.ExampleDto;
import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.TranslationDto;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.card.core.model.enums.LearningItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
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
public class CardDto {
    UUID id;
    String publicId;
    UUID userId;

    @NotBlank
    String entry;

    String normalizedForm;
    String lemma;
    LearningItemType itemType;
    String partOfSpeech;
    String selectedSense;
    String sourceContext;
    Set<LearningIntent> learningIntents;

    @NotBlank
    String language;

    @NotEmpty
    TranslationDto[] translations;

    String notes;
    TagDto[] tags;
    ExampleDto[] examples;
    Instant createdAt;
    Instant updatedAt;
    int iteration;
    int priority;
    boolean activeLearning;
    boolean irregularPlural;
    boolean irregularSpelling;
    boolean falseFriend;
}
