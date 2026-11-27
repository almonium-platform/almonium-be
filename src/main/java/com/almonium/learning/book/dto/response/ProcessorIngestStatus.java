package com.almonium.learning.book.dto.response;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import tools.jackson.databind.JsonNode;

/**
 * The processor's own account of a library or source ingest, mirrored read-only. {@code completedStages} names the
 * pipeline stages that succeeded and {@code currentStage} the one running, when the processor reports them.
 * {@code sourceKind} and {@code sourcePageUrl} name the text the edition was made from, which after a fallback is not
 * the one first asked for; null when the processor does not say.
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
        String currentStage,
        String sourceKind,
        String sourcePageUrl) {

    /** The status as the processor writes it (snake_case), whether polled from it or pushed by the factory. */
    public static ProcessorIngestStatus from(JsonNode body) {
        List<String> completed = new ArrayList<>();
        body.path("completed_stages").forEach(stage -> completed.add(stage.asString("")));
        String current = body.path("current_stage").asString("");
        return new ProcessorIngestStatus(
                uuid(body.path("edition_id")),
                body.path("slug").asString(""),
                body.path("status").asString(""),
                body.path("phase").asString(""),
                body.path("progress").asInt(0),
                body.path("error").asString(""),
                instant(body.path("published_at")),
                uuid(body.path("published_book_id")),
                completed,
                current.isBlank() ? null : current,
                blankToNull(body.path("source").path("kind").asString("")),
                blankToNull(body.path("source").path("page_url").asString("")));
    }

    private static String blankToNull(String text) {
        return text.isBlank() ? null : text;
    }

    private static UUID uuid(JsonNode node) {
        String text = node.asString("");
        return text.isBlank() ? null : UUID.fromString(text);
    }

    private static Instant instant(JsonNode node) {
        String text = node.asString("");
        return text.isBlank() ? null : OffsetDateTime.parse(text).toInstant();
    }

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

    public ProcessorIngestStatus(
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
        this(
                editionId,
                slug,
                status,
                phase,
                progress,
                error,
                publishedAt,
                publishedBookId,
                completedStages,
                currentStage,
                null,
                null);
    }
}
