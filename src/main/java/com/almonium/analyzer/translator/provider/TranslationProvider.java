package com.almonium.analyzer.translator.provider;

import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;

public interface TranslationProvider {
    String providerName();

    TranslationCardDto translate(String entry, Language sourceLanguage, Language targetLanguage);
}
