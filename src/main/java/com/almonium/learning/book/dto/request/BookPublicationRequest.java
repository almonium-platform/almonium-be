package com.almonium.learning.book.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookPublicationRequest(
        @NotBlank String editionSlug,
        @NotBlank String sourceHash,
        @NotBlank String workSlug,
        @NotBlank String title,
        @NotBlank String author,
        @NotNull Language originalLanguage,
        @NotNull Language language,
        @NotBlank String editionType,
        String sourceEditionSlug,
        String translator,
        Integer firstPublishedYear,
        int wordCount) {}
