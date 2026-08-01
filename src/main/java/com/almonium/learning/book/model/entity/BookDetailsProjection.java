package com.almonium.learning.book.model.entity;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;

public interface BookDetailsProjection {
    UUID getId();

    String getTitle();

    String getAuthor();

    Integer getPublicationYear();

    String getCoverImageUrl();

    Integer getWordCount();

    Double getRating();

    Language getLanguage();

    CEFR getLevelFrom();

    CEFR getLevelTo();

    Integer getProgressPercentage();

    Boolean getHasTranslation();

    Boolean getHasParallelTranslation();

    Boolean getIsTranslation();

    String getDescription();

    Language getOriginalLanguage();

    UUID getOriginalId();

    String getTranslator();
}
