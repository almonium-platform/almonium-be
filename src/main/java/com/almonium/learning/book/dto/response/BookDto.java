package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;

public record BookDto(
        UUID id,
        String workSlug,
        String title,
        String author,
        Integer publicationYear,
        String coverUrl,
        Integer wordCount,
        Language language,
        CEFR cefrLevel,
        Integer progressPercentage,
        Boolean hasTranslation,
        Boolean hasParallelTranslation,
        Boolean isTranslation) {}
