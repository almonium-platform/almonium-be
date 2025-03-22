package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;

public record BookDto(
        Long id,
        String title,
        String author,
        Integer publicationYear,
        String coverImageUrl,
        Integer wordCount,
        Double rating,
        Language language,
        CEFR levelFrom,
        CEFR levelTo,
        Integer progressPercentage,
        Boolean hasTranslation,
        Boolean hasParallelTranslation,
        Boolean isTranslation) {}
