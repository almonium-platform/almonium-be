package com.linguatool.model.dto.api.response.oxford;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

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
