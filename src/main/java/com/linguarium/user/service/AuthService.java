package com.linguarium.user.service;

import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegisterRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;

public interface AuthService {
    void register(RegisterRequest registrationRequest);

    JwtAuthResponse login(LoginRequest loginRequest);

    LocalUser authenticateProviderRequest(OAuth2UserInfo userInfo);
}
