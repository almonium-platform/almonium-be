package com.almonium.learning.catalog.model;

import com.almonium.learning.rights.model.AuthorFacts;

/**
 * An author as the catalogue stores them, inside the work's {@code authors} JSON: the name to show and the facts the
 * copyright engine needs. {@code wikidataId} is null for an author known only from Project Gutenberg.
 */
public record CatalogAuthor(
        String name,
        String wikidataId,
        Integer birthYear,
        Integer deathYear,
        boolean diedForFrance,
        Integer rehabilitatedYear) {

    public AuthorFacts facts() {
        return new AuthorFacts(name, birthYear, deathYear, diedForFrance, rehabilitatedYear);
    }
}
