package com.almonium.learning.catalog.dto.response;

import java.util.List;
import java.util.UUID;

/**
 * One search result from the catalogue (W1, W2). {@code state} is what the reader can do with it: open it from the
 * shelf, learn the shelf has it but not for their country, want it, or learn it is under copyright. {@code freeFrom}
 * is the year it becomes free where we make books, when known. {@code readers} counts distinct members wanting it in
 * the language searched for (null below three, or without a language); {@code wantedByMe} is the viewer's own want.
 * {@code readerFreeFrom} is the year the reader can open it, free where it is made and in {@code readerCountry}, when
 * exact (W6); {@code freeIn} names up to three countries where it is already free.
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
        Integer freeFrom,
        Integer readers,
        boolean wantedByMe,
        String readerCountry,
        Integer readerFreeFrom,
        List<String> freeIn) {

    public enum CatalogState {
        ON_SHELF,
        NOT_IN_YOUR_COUNTRY,
        WANTABLE,
        UNDER_COPYRIGHT
    }
}
