package com.linguarium.config.security.oauth2.userinfo;

import com.linguarium.auth.dto.AuthProvider;
import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public AuthProvider getProvider() {
        return AuthProvider.GOOGLE;
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
    public String getFirstName() {
        return getStringAttribute("given_name");
    }

    @Override
    public String getLastName() {
        return getStringAttribute("family_name");
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
