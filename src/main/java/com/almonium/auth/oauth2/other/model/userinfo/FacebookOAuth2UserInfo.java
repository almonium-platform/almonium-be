package com.almonium.auth.oauth2.other.model.userinfo;

import com.almonium.auth.common.model.enums.AuthProviderType;
import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {
    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public AuthProviderType getProvider() {
        return AuthProviderType.FACEBOOK;
    }

    @Override
    public String getId() {
        return getStringAttribute("id");
    }

    @Override
    public String getName() {
        return getStringAttribute("name");
    }

    @Override
    public String getFirstName() {
        return getStringAttribute("first_name");
    }

    @Override
    public String getLastName() {
        return getStringAttribute("last_name");
    }

    @Override
    public String getEmail() {
        return getStringAttribute("email");
    }

    @Override
    public String getImageUrl() {
        return getNestedStringAttribute("picture.data.url");
    }

    @Override
    public boolean isEmailVerified() {
        return true;
    }
}
