package com.almonium.learning.book.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** The owner's confirmed details for a private import. */
public record BookImportMetadataRequest(
        @NotBlank @Size(max = 500) String title,
        @NotBlank @Size(max = 300) String author,
        String description,
        @NotNull Language language,
        @Min(1) @Max(9999) Integer publicationYear) {}
