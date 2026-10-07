package com.almonium.learning.catalog.service;

import com.almonium.learning.catalog.model.CatalogAuthor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** One work as an import assembles it before writing: the row, and every title and name it can be found by. */
final class CatalogRecord {
    UUID id;
    String wikidataId;
    Integer gutenbergId;
    String title;
    String originalTitle;
    String originalLanguage;
    Integer publicationYear;
    List<CatalogAuthor> authors = new ArrayList<>();
    int popularity;
    String publisher;
    boolean literary;
    final List<Title> titles = new ArrayList<>();
    final List<Source> sources = new ArrayList<>();

    record Title(String text, String language, String kind) {}

    /** Where a text of the work can be fetched: a Gutenberg book id or a Wikisource page URL. */
    record Source(
            String kind,
            String language,
            String reference,
            String label,
            boolean translation,
            List<CatalogAuthor> translators) {}

    static final String GUTENBERG = "GUTENBERG";
    static final String WIKISOURCE = "WIKISOURCE";

    static final String TITLE = "TITLE";
    static final String AUTHOR = "AUTHOR";
    static final String FULL = "FULL";

    String authorDisplay() {
        if (authors.isEmpty()) {
            return null;
        }
        String joined = String.join(
                ", ",
                authors.stream()
                        .map(CatalogAuthor::name)
                        .filter(name -> name != null && !name.isBlank())
                        .toList());
        return joined.isEmpty() ? null : truncate(joined, 1000);
    }

    void addTitle(String text, String language) {
        if (text != null && !text.isBlank()) {
            titles.add(new Title(truncate(text.trim(), 1000), language, TITLE));
        }
    }

    static String truncate(String text, int length) {
        return text.length() <= length ? text : text.substring(0, length);
    }
}
