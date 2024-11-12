package com.almonium.analyzer.client.freedictionary.dto;

import static lombok.AccessLevel.PRIVATE;

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
public class FDMeaning {
    String partOfSpeech;
    String[] synonyms;
    String[] antonyms;
    FDDefinition[] definitions;
}
