package com.almonium.learning.catalog.service;

import java.util.List;

/**
 * A library beyond Gutenberg and Wikisource whose texts the catalogue lists as sources: Standard Ebooks, Wolne
 * Lektury, and the ones that follow them. Each tells its texts by title and author; the import attaches them to the
 * notable works it already knows (never making a work from them, since a library rarely records the author's dates
 * the copyright engine needs).
 */
public interface LibraryCatalogSource {
    /**
     * One text of a library: the kind it is a source of, the reference the kind's addresses are built from, its
     * title and author as the library writes them, and the language of the text.
     */
    record LibraryText(String kind, String reference, String title, String author, String language) {}

    String kind();

    List<LibraryText> fetch();
}
