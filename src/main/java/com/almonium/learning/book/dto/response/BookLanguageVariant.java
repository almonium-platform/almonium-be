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
}
