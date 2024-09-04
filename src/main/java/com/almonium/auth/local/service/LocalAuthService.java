package com.almonium.auth.local.service;

import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.response.JwtAuthResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface LocalAuthService {
    void register(LocalAuthRequest registrationRequest);

    JwtAuthResponse login(LocalAuthRequest localAuthRequest, HttpServletResponse response);

    void verifyEmail(String token);

    void requestPasswordReset(String email);

    void resetPassword(String token, String newPassword);
}
