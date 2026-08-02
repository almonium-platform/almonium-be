package com.almonium.learning.book.model.entity;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.UUID;

public interface BookMiniProjection {
    UUID getId();

    String getEditionSlug();

    Language getLanguage();
}
