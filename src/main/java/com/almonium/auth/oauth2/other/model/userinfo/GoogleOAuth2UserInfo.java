package com.almonium.auth.oauth2.other.model.userinfo;

import com.almonium.auth.common.model.enums.AuthProviderType;
import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public AuthProviderType getProvider() {
        return AuthProviderType.GOOGLE;
    }

    @Override
    public String getId() {
        return getStringAttribute(SUB);
    }

    @Override
    public String getName() {
        return getStringAttribute("name");
    }

    @Override
    public String getFirstName() {
        return getStringAttribute("given_name");
    }

    @Override
    public String getLastName() {
        return getStringAttribute("family_name");
    }

    @Override
    public String getEmail() {
        return getStringAttribute(EMAIL);
    }

    @Override
    public String getAvatarUrl() {
        return getStringAttribute("picture");
    }

    @Override
    public boolean isEmailVerified() {
        return (boolean) attributes.get(EMAIL_VERIFIED);
    }
}
