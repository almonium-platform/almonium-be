package com.almonium.card.core.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.ExampleDto;
import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.TranslationDto;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.card.core.model.enums.LearningItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;
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
    Language language;

    @NotEmpty
    TranslationDto[] translations;

    String notes;
    String normalizedForm;
    String lemma;
    LearningItemType itemType;
    String partOfSpeech;
    String selectedSense;
    String sourceContext;
    Set<LearningIntent> learningIntents;
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
