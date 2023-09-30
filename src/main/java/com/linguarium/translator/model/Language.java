package com.linguarium.translator.model;

import lombok.Getter;

@Getter
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

    private final String code;

    public static Language fromString(String code) {
        for (Language language : Language.values()) {
            if (language.code.equalsIgnoreCase(code)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Can't find Language for value: " + code);
    }
}
