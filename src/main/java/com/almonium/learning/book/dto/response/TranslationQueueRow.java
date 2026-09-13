package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * One book-and-language pair in the /ops queue. Asks are grouped here so the count is the whole demand signal;
 * {@code estimatedCostUsd} is null when the processor could not price the pair, and {@code job} is null while the
 * pair is still open.
 */
public record TranslationQueueRow(
        UUID bookId,
        String bookTitle,
        String bookAuthor,
        String editionSlug,
        Language sourceLanguage,
        Language language,
        int asks,
        int premiumAsks,
        int freeAsks,
        BigDecimal estimatedCostUsd,
        TranslationJobDto job) {}
