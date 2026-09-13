package com.almonium.learning.book.dto.response;

import java.time.Instant;
import java.util.UUID;

/** The processor's own account of a library ingest, mirrored read-only. */
public record ProcessorIngestStatus(
        UUID editionId,
        String slug,
        String status,
        String phase,
        int progress,
        String error,
        Instant publishedAt,
        UUID publishedBookId) {}
