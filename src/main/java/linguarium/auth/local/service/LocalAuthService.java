package linguarium.auth.local.service;

import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;

public interface LocalAuthService {
    void register(LocalAuthRequest registrationRequest);

    JwtAuthResponse login(LocalAuthRequest localAuthRequest);

    void verifyEmail(String token);

    void requestPasswordReset(String email);

    void resetPassword(String token, String newPassword);
}
