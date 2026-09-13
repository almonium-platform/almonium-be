package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.TranslationOrderStatus;
import java.time.Instant;
import java.util.UUID;

/**
 * One request in the caller's list. {@code fulfilledBookId} and {@code fulfilledEditionSlug} name the published
 * translation to open, and are only set once the request is {@code READY}; {@code seenAt} is when the reader first
 * opened it, so the fulfilment notice knows whether it is still news.
 */
public record TranslationOrderDto(
        UUID id,
        UUID userId,
        UUID bookId,
        String bookTitle,
        String bookAuthor,
        String bookEditionSlug,
        Language language,
        TranslationOrderStatus status,
        UUID fulfilledBookId,
        String fulfilledEditionSlug,
        Instant createdAt,
        Instant seenAt) {}
