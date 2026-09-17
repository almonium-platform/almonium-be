package com.almonium.learning.book.dto.response;

/**
 * What the ask sheet learns as the reader types (G19): whether the public-domain index knows the work, how many
 * distinct members already asked, and the shelf copy when the library already has it in that language.
 */
public record BookLookupDto(
        String title,
        String author,
        Integer gutenbergId,
        /** gutenberg when the index matched, unknown otherwise. */
        String publicDomain,
        int askers,
        LibraryMatch onShelf) {}
