package com.linguarium.analyzer.dto;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.analyzer.model.CEFR;
import com.linguarium.card.dto.CardDto;
import com.linguarium.translator.dto.TranslationCardDto;
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
