package com.almonium.learning.catalog.dto.response;

import java.util.UUID;

/**
 * One search result from the catalogue (W1, W2). {@code state} is what the reader can do with it: open it from the
 * shelf, learn the shelf has it but not for their country, want it, or learn it is under copyright. {@code freeFrom} is the year it becomes free where we make books,
 * when that is known.
 */
public record CatalogHit(
        UUID id,
        String title,
        String originalTitle,
        String originalLanguage,
        String author,
        Integer year,
        CatalogState state,
        String workSlug,
        Integer freeFrom) {

    public enum CatalogState {
        ON_SHELF,
        NOT_IN_YOUR_COUNTRY,
        WANTABLE,
        UNDER_COPYRIGHT
    }
}
