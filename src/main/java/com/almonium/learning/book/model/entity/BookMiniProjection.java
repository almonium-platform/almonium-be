package com.almonium.learning.book.model.entity;

import com.almonium.analyzer.translator.model.enums.Language;

public interface BookMiniProjection {
    Long getId();

    Language getLanguage();
}
