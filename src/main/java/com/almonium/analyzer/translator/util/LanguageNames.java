package com.almonium.analyzer.translator.util;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.Locale;

/** "Ukrainian", not "UK": the code is ours, the name is the reader's. */
public final class LanguageNames {
    private LanguageNames() {}

    public static String englishName(Language language) {
        String code = language.name().toLowerCase(Locale.ROOT);
        String name = Locale.forLanguageTag(code).getDisplayLanguage(Locale.ENGLISH);
        return name == null || name.isBlank() || name.equalsIgnoreCase(code) ? language.name() : name;
    }
}
