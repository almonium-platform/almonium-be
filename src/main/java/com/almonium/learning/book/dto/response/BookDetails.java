package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;
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
public class BookDetails {
    UUID id;
    String editionSlug;
    String workSlug;
    String title;
    String author;
    String description;
    Integer publicationYear;
    String coverUrl;
    Integer wordCount;
    Language language;
    CEFR cefrLevel;
    Integer progressPercentage;
    Boolean hasTranslation;
    Boolean hasParallelTranslation;
    Boolean isTranslation;
    List<BookLanguageVariant> languageVariants;
    boolean favorite;
    Language originalLanguage;
    UUID originalId;
    String translator;
}
