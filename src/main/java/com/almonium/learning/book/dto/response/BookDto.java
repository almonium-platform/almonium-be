package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;

public record BookDto(
        Long id,
        String title,
        String author,
        int publicationYear,
        String coverImageUrl,
        int wordCount,
        double rating,
        Language language,
        CEFR levelFrom,
        CEFR levelTo,
        Integer progressPercentage) {}
