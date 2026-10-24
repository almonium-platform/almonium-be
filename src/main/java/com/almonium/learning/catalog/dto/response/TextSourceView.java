package com.almonium.learning.catalog.dto.response;

import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.rights.model.RightsStatus;
import java.util.List;
import java.util.UUID;

/**
 * One place a work's text can be fetched from, judged for copyright where we make books: the original's authors and,
 * for a translation, its translators. A translation whose translators are not known is unknown, never free.
 *
 * @param edition one edition's text; a Wikisource page about the whole work may only list its editions
 * @param pageUrl the human page (Gutenberg's book page, the Wikisource page)
 * @param downloadUrl the EPUB the book processor fetches
 * @param shape the processor's verdict on the file's shape ("ok", "suspect", "unreadable"), null when never measured
 */
public record TextSourceView(
        UUID id,
        String kind,
        String language,
        String label,
        boolean translation,
        List<CatalogAuthor> translators,
        boolean edition,
        String pageUrl,
        String downloadUrl,
        RightsStatus status,
        Integer freeFrom,
        String shape) {}
