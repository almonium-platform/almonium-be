package com.almonium.learning.rights.model;

import java.util.List;

/**
 * Everything the copyright engine knows about a work: when it was first published and who wrote it. An empty author
 * list means nobody is recorded, which the engine treats as one author it knows nothing about.
 */
public record RightsFacts(Integer publicationYear, List<AuthorFacts> authors) {
    public RightsFacts {
        authors = authors == null ? List.of() : List.copyOf(authors);
    }
}
