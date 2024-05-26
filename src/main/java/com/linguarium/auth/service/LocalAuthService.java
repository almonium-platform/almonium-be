package com.linguarium.auth.service;

import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegisterRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;

public interface LocalAuthService {
    void register(RegisterRequest registrationRequest);

    JwtAuthResponse login(LoginRequest loginRequest);
}
