package com.almonium.learning.book.dto.request;

import java.util.Map;
import java.util.UUID;

public record BookImportEventRequest(
        UUID importId,
        UUID ownerId,
        String status,
        String title,
        int progress,
        int wordCount,
        String error,
        Metadata metadata) {

    /**
     * Bibliographic details as the processor currently holds them. {@code language} is the processor's
     * lower-case ISO code; {@code detected} is false until the metadata stage has run.
     */
    public record Metadata(
            String title,
            String author,
            String description,
            String language,
            Integer publicationYear,
            Map<String, String> provenance,
            boolean detected) {}
}
