package com.linguatool.model.entity.lang;

public enum Language {
    ENGLISH("EN"),
    GERMAN("DE"),
    FRENCH("FR"),
    SPANISH("ES"),
    UKRAINIAN("UK"),
    RUSSIAN("RU"),
    POLISH("PL");

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private final String code;

    public static Language fromString(String text) {
        for (Language language : Language.values()) {
            if (language.code.equalsIgnoreCase(text)) {
                return language;
            }
        }
        return null;
    }
}
