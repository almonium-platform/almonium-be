package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.LibrarySuggestionStatus;
import java.time.Instant;
import java.util.UUID;

/**
 * One work in the /ops suggestion queue, grouped by title and author so the import count is the demand signal.
 * {@code id} is the oldest suggestion of the group and is what every decision endpoint takes.
 */
public record LibrarySuggestionRow(
        UUID id,
        String title,
        String author,
        Language language,
        Integer publicationYear,
        String publicDomainHint,
        int imports,
        int premiumCount,
        int freeCount,
        LibrarySuggestionStatus status,
        LibraryMatch libraryMatch,
        IngestJobDto job,
        Instant createdAt) {}
