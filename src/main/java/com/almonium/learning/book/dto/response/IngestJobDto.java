package com.almonium.learning.book.dto.response;

import java.time.Instant;
import java.util.UUID;

public record IngestJobDto(
        UUID processorEditionId,
        String processorEditionSlug,
        String phase,
        int progress,
        String error,
        Instant decidedAt,
        UUID publishedBookId) {}
