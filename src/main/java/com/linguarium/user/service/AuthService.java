package com.linguarium.user.service;

import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegisterRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.user.model.User;

public interface AuthService {
    void register(RegisterRequest registrationRequest);

    JwtAuthResponse login(LoginRequest loginRequest);

    User authenticateProviderRequest(OAuth2UserInfo userInfo);
}
