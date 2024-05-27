package com.linguarium.engine.client.oxford.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
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
public class OxfordLexicalEntry {
    String language;
    List<OxfordEntry> entries;
    OxfordLexicalCategory lexicalCategory;
    OxfordPhrase[] phrases;
    String text;
}
