package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.LibrarySuggestionStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * The owner's view of their suggestion. The library fields are set once it is {@code PUBLISHED};
 * {@code libraryParallelLanguages} are the languages the library copy can be read alongside.
 */
public record LibrarySuggestionDto(
        UUID id,
        LibrarySuggestionStatus status,
        Instant createdAt,
        String libraryEditionSlug,
        String libraryTitle,
        List<Language> libraryParallelLanguages) {}
