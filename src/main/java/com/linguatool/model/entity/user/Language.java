package com.linguatool.model.entity.user;

public enum Language {
    ENGLISH("EN"), GERMAN("DE"), UKRAINIAN("UK"), RUSSIAN("RU");

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
