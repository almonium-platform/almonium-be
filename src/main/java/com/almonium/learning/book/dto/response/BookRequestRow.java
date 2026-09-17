package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.BookRequestStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * One work in the /ops book-request queue (G20), grouped by title, author and language so the asker count is the
 * row's weight. {@code id} is the oldest ask of the group and is what the decision endpoints take.
 * {@code publicDomain} is the lookup result stored at ask time: "gutenberg" with an id, "yes" by year, else
 * "unlikely". {@code haveLanguages} lists the languages the work already exists in when {@code workSlug} is set.
 */
public record BookRequestRow(
        UUID id,
        String title,
        String author,
        Language language,
        String workSlug,
        List<Language> haveLanguages,
        Integer publicationYear,
        Integer gutenbergId,
        String publicDomain,
        int askers,
        BookRequestStatus status,
        String editorialUrl,
        Instant askedAt,
        Instant decidedAt) {}
