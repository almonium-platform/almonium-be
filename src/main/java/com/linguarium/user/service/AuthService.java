package com.linguarium.user.service;

import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegistrationRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.user.model.User;
import java.util.Map;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public interface AuthService {
    User register(RegistrationRequest registrationRequest);

    JwtAuthResponse login(LoginRequest loginRequest);

    LocalUser processProviderAuth(
            String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}
