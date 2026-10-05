package com.almonium.learning.catalog.model;

import com.almonium.learning.rights.model.AuthorFacts;

/**
 * An author as the catalogue stores them, inside the work's {@code authors} JSON: the name to show and the facts the
 * copyright engine needs. {@code wikidataId} is null for an author known only from Project Gutenberg;
 * {@code repressed} marks a Soviet author who died in the purges and may have been rehabilitated after death.
 */
public record CatalogAuthor(
        String name,
        String wikidataId,
        Integer birthYear,
        Integer deathYear,
        boolean diedForFrance,
        Integer rehabilitatedYear,
        boolean repressed) {

    public CatalogAuthor(
            String name,
            String wikidataId,
            Integer birthYear,
            Integer deathYear,
            boolean diedForFrance,
            Integer rehabilitatedYear) {
        this(name, wikidataId, birthYear, deathYear, diedForFrance, rehabilitatedYear, false);
    }

    public AuthorFacts facts() {
        return new AuthorFacts(name, birthYear, deathYear, diedForFrance, rehabilitatedYear, repressed);
    }
}
