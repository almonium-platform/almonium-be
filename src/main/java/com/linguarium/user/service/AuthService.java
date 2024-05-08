package com.linguarium.user.service;

import com.linguarium.auth.dto.request.LocalRegisterRequest;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.auth.model.LocalUser;
import java.util.Map;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public interface AuthService {
    void register(LocalRegisterRequest registrationRequest);

    JwtAuthResponse login(LoginRequest loginRequest);

    LocalUser processProviderAuth(
            String provider, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}
