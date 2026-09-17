package com.almonium.learning.book.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** What the ask sheet posts (G19); {@code workSlug} is set when the ask comes from a book page for a language it lacks. */
public record BookRequestAsk(
        @NotBlank @Size(max = 500) String title,
        @Size(max = 300) String author,
        @NotNull Language language,
        @Size(max = 180) String workSlug) {}
