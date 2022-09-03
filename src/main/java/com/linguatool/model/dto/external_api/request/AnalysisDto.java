package com.linguatool.model.dto.external_api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class AnalysisDto {
    Double frequency;
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
}
