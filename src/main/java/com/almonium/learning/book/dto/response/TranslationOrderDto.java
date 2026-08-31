package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.TranslationOrderStatus;
import java.time.Instant;
import java.util.UUID;

/**
 * One request in the caller's list. {@code fulfilledBookId} is the published translation
 * to open, and is only set once the request is {@code READY}.
 */
public record TranslationOrderDto(
        UUID id,
        UUID userId,
        UUID bookId,
        String bookTitle,
        String bookAuthor,
        Language language,
        TranslationOrderStatus status,
        UUID fulfilledBookId,
        Instant createdAt) {}
