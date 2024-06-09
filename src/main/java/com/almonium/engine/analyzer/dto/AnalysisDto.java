package com.almonium.engine.analyzer.dto;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.dto.CardDto;
import com.almonium.engine.analyzer.model.enums.CEFR;
import com.almonium.engine.translator.dto.TranslationCardDto;
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
public class AnalysisDto {
    Double frequency;
    CEFR cefr;
    String[] lemmas;
    String[] posTags;
    String[] adjectives;
    String[] nouns;
    CardDto[] foundCards;
    String[] homophones;
    String[] family;
    String[] syllables;
    Boolean isProper;
    Boolean isForeignWord;
    Boolean isPlural;
    TranslationCardDto translationCards;
}
