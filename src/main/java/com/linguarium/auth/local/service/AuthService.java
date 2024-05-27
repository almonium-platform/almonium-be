package com.linguarium.auth.local.service;

import com.linguarium.auth.local.dto.request.LoginRequest;
import com.linguarium.auth.local.dto.request.RegisterRequest;
import com.linguarium.auth.local.dto.response.JwtAuthResponse;

public interface AuthService {
    void register(RegisterRequest registrationRequest);

    JwtAuthResponse login(LoginRequest loginRequest);
}
