package com.linguarium.auth.dto;

import lombok.Getter;

@Getter
public enum SocialProvider {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    LOCAL("local");

    private final String providerType;

    SocialProvider(final String providerType) {
        this.providerType = providerType;
    }

    public static SocialProvider toSocialProvider(String providerId) {
        for (SocialProvider socialProvider : SocialProvider.values()) {
            if (socialProvider.getProviderType().equals(providerId)) {
                return socialProvider;
            }
        }
        return SocialProvider.LOCAL;
    }
}
