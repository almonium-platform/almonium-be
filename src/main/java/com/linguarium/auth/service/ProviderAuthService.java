package com.linguarium.auth.service;

import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.user.model.User;

public interface ProviderAuthService {
    User authenticate(OAuth2UserInfo userInfo);
}
