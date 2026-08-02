package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;

public record BookDto(
        UUID id,
        String editionSlug,
        String workSlug,
        String title,
        String author,
        String description,
        Integer publicationYear,
        String coverUrl,
        Integer wordCount,
        Language language,
        CEFR cefrLevel,
        Integer progressPercentage,
        boolean hasTranslation,
        boolean hasParallelTranslation,
        boolean isTranslation) {}
