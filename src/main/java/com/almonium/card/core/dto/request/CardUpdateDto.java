package com.almonium.card.core.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.ExampleDto;
import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.TranslationDto;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
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
public class CardUpdateDto {
    @NotNull
    UUID id;

    String entry;
    TranslationDto[] translations;
    String notes;
    TagDto[] tags;
    ExampleDto[] examples;
    Instant createdAt;
    Instant lastRepeat;
    Integer iteration;
    UUID userId;
    Integer priority;
    UUID[] deletedTranslationsIds;
    UUID[] deletedExamplesIds;
    Instant updatedAt;
    Boolean activeLearning;
    Boolean falseFriend;
    Boolean irregularPlural;
    Boolean irregularSpelling;
    Language language;
}
