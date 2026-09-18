package com.almonium.learning.book.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import java.time.Instant;
import java.util.List;

/**
 * The certificate as both its owner and a stranger see it: nothing beyond what the page shows. The username and
 * the counts come from the reader's own record; nothing else about the account is exposed.
 */
public record BookCertificateDto(
        String username,
        String editionSlug,
        String title,
        String author,
        Language language,
        List<String> words,
        int wordsRead,
        int wordsSaved,
        Instant finishedAt,
        boolean publicPage) {}
