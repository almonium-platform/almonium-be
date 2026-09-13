package com.almonium.learning.book.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/** The processor's own account of a translation job, mirrored read-only. */
public record ProcessorTranslationStatus(
        UUID editionId,
        String editionSlug,
        String status,
        String phase,
        Integer progressCompleted,
        Integer progressTotal,
        BigDecimal estimatedCostUsd,
        String error,
        Instant startedAt,
        Instant publishedAt,
        UUID publishedBookId) {}
