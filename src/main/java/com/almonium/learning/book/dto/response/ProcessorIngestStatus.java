package com.almonium.learning.book.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * The processor's own account of a library or source ingest, mirrored read-only. {@code completedStages} names the
 * pipeline stages that succeeded and {@code currentStage} the one running, when the processor reports them.
 */
public record ProcessorIngestStatus(
        UUID editionId,
        String slug,
        String status,
        String phase,
        int progress,
        String error,
        Instant publishedAt,
        UUID publishedBookId,
        List<String> completedStages,
        String currentStage) {

    public ProcessorIngestStatus(
            UUID editionId,
            String slug,
            String status,
            String phase,
            int progress,
            String error,
            Instant publishedAt,
            UUID publishedBookId) {
        this(editionId, slug, status, phase, progress, error, publishedAt, publishedBookId, List.of(), null);
    }
}
