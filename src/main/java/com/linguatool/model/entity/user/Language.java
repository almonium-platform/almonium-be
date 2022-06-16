package com.linguatool.model.entity.user;

import javax.persistence.Embeddable;

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
        for (Language b : Language.values()) {
            if (b.code.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
