package com.almonium.learning.wanted.dto.response;

import java.util.UUID;

/**
 * A wanted work the factory may start: nobody is preparing it, and it has a free text in a language the book
 * processor reads. The source is the best one: in the wanted language if a free one exists, else the original.
 */
public record FactoryQueueRow(
        UUID catalogWorkId,
        String language,
        String title,
        String author,
        Integer publicationYear,
        String workLanguage,
        int readers,
        Source source) {

    public record Source(UUID id, String kind, String language, String pageUrl, String downloadUrl) {}
}
