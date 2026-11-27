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
    UUID senseKey;
    String sourceContext;
    /** The library edition the word was met in, sent by the reader and by nothing else. */
    UUID sourceBookId;

    /** That edition's title and the chapter the word was met in, for the line that says where a word came from. */
    @jakarta.validation.constraints.Size(max = 500)
    String sourceBookTitle;

    @jakarta.validation.constraints.Min(0)
    Integer sourceChapter;

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
