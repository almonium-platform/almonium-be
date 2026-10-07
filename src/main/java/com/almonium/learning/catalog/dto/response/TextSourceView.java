package com.almonium.learning.catalog.dto.response;

import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.rights.model.RightsStatus;
import java.util.List;
import java.util.UUID;

/**
 * One place a work's text can be fetched from, judged for copyright where we make books: the original's authors and,
 * for a translation, its translators. A translation whose translators are not known is unknown, never free.
 *
 * @param pageUrl the human page (Gutenberg's book page, the Wikisource page)
 * @param downloadUrl the EPUB the book processor fetches
 */
public record TextSourceView(
        UUID id,
        String kind,
        String language,
        String label,
        boolean translation,
        List<CatalogAuthor> translators,
        String pageUrl,
        String downloadUrl,
        RightsStatus status,
        Integer freeFrom) {}
