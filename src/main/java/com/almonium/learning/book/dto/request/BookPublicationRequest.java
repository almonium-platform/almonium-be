package com.almonium.learning.book.dto.request;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookPublicationRequest(
        @NotBlank String editionSlug,
        @NotBlank String sourceHash,
        @NotBlank String workSlug,
        @NotBlank String title,
        @NotBlank String author,
        String description,
        @NotNull Language originalLanguage,
        @NotNull Language language,
        @NotBlank String editionType,
        String sourceEditionSlug,
        String translator,
        int publicationYear,
        String coverUrl,
        @NotNull CEFR cefrLevel,
        int wordCount) {}
