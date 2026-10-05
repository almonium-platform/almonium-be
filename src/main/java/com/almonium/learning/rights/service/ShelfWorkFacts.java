package com.almonium.learning.rights.service;

import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.rights.model.RightsFacts;

/**
 * What the copyright engine judges a shelf work by: the facts of the catalogue work it is linked to, or, when it is
 * not linked, only what the edition itself says (its publication year, its author's name without dates).
 */
public record ShelfWorkFacts(String workSlug, RightsFacts facts, CatalogWork catalogWork) {
    public boolean linked() {
        return catalogWork != null;
    }
}
