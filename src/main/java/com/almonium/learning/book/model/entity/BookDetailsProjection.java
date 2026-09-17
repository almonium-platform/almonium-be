package com.almonium.learning.book.model.entity;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;

public interface BookDetailsProjection {
    UUID getId();

    String getEditionSlug();

    String getWorkSlug();

    String getTitle();

    String getAuthor();

    String getDescription();

    Integer getPublicationYear();

    String getCoverUrl();

    Integer getWordCount();

    Language getLanguage();

    CEFR getCefrLevel();

    /** original, adaptation, machine_translation or human_translation, as the processor names it. */
    String getEditionType();

    Integer getProgressPercentage();

    Boolean getHasTranslation();

    Boolean getHasParallelTranslation();

    Boolean getIsTranslation();

    Language getOriginalLanguage();

    UUID getOriginalId();

    /** The original's title when this edition is titled differently (a translation); null otherwise. */
    String getOriginalTitle();

    String getTranslator();
}
