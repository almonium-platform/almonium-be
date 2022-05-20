package com.linguatool.model.dto.api.response.oxford;

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
public class OxfordSense {
    String id;
    String[] definitions;
    String[] shortDefinitions;
    OxfordExample[] examples;
    OxfordSynonym[] synonyms;
    OxfordSense[] subsenses;
}
