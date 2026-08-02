package com.almonium.learning.book.model.entity;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;

public interface BookDetailsProjection {
    UUID getId();

    String getWorkSlug();

    String getTitle();

    String getAuthor();

    Integer getPublicationYear();

    String getCoverUrl();

    Integer getWordCount();

    Language getLanguage();

    CEFR getCefrLevel();

    Integer getProgressPercentage();

    Boolean getHasTranslation();

    Boolean getHasParallelTranslation();

    Boolean getIsTranslation();

    Language getOriginalLanguage();

    UUID getOriginalId();

    String getTranslator();
}
