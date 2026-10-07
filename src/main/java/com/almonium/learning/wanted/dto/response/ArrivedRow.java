package com.almonium.learning.wanted.dto.response;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.time.Instant;
import java.util.UUID;

/** A wanted work that reached the shelf in the last two weeks (W3, "Recently arrived"). */
public record ArrivedRow(
        UUID catalogWorkId,
        String title,
        String author,
        Language language,
        String editionSlug,
        CEFR cefrLevel,
        String coverUrl,
        Instant arrivedAt) {}
