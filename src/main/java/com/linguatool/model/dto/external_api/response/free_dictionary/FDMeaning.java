package com.linguatool.model.dto.external_api.response.free_dictionary;

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
public class FDMeaning {
    String partOfSpeech;
    String[] synonyms;
    String[] antonyms;
    FDDefinition[] definitions;
}
