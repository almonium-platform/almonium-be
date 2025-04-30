package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;
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
    Long id;
    String title;
    String author;
    Integer publicationYear;
    String coverImageUrl;
    Integer wordCount;
    Double rating;
    Language language;
    CEFR levelFrom;
    CEFR levelTo;
    Integer progressPercentage;
    Boolean hasTranslation;
    Boolean hasParallelTranslation;
    Boolean isTranslation;
    String description;
    List<BookLanguageVariant> languageVariants;
    Language orderLanguage;
    boolean favorite;
    Language originalLanguage;
    Long originalId;
    String translator;
}
