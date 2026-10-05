package com.almonium.learning.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** The book processor asking whether a work may be prepared, before it spends anything on it. */
public record BookRightsCheckRequest(
        @Size(max = 180) String workSlug,
        @NotBlank @Size(max = 500) String title,
        @Size(max = 300) String author,
        Integer publicationYear) {}
