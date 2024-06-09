package com.almonium.auth.oauth2.model.userinfo;

import com.almonium.auth.common.enums.AuthProviderType;
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
        return getStringAttribute("sub");
    }

    @Override
    public String getName() {
        return getStringAttribute("name");
    }

    @Override
    public String getEmail() {
        return getStringAttribute("email");
    }

    @Override
    public String getImageUrl() {
        return getStringAttribute("picture");
    }
}
