package com.almonium.auth.oauth2.other.model.enums;

public enum OAuth2Intent {
    LINK,
    SIGN_IN,
    REAUTH;

    public static OAuth2Intent fromString(String intent) {
        for (OAuth2Intent intentEnumValue : values()) {
            if (intentEnumValue.name().equalsIgnoreCase(intent)) {
                return intentEnumValue;
            }
        }
        return null;
    }
}
