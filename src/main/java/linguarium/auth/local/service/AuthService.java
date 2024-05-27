package linguarium.auth.local.service;

import linguarium.auth.local.dto.request.LoginRequest;
import linguarium.auth.local.dto.request.RegisterRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;

public interface AuthService {
    void register(RegisterRequest registrationRequest);

    JwtAuthResponse login(LoginRequest loginRequest);
}
