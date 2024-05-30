package linguarium.auth.local.service;

import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;

public interface AuthService {
    JwtAuthResponse register(LocalAuthRequest registrationRequest);

    JwtAuthResponse login(LocalAuthRequest localAuthRequest);

    void linkLocalAuth(Long userId, LocalAuthRequest localAuthRequest);
}
