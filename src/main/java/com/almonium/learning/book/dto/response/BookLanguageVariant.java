package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookLanguageVariant {
    UUID id;
    String editionSlug;
    Language language;
    String editionType;
    String literaryRegister;
    String cefrLevel;
    String sourceEditionSlug;
    String editionNote;

    public BookLanguageVariant(UUID id, String editionSlug, Language language) {
        this.id = id;
        this.editionSlug = editionSlug;
        this.language = language;
    }
}
