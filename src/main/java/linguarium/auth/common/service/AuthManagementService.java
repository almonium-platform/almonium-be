package linguarium.auth.common.service;

import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.model.entity.LocalPrincipal;
import linguarium.auth.local.model.enums.TokenType;

public interface AuthManagementService {
    void linkLocalAuth(Long userId, LocalAuthRequest localAuthRequest);

    void unlinkAuthMethod(Long userId, AuthProviderType providerType);

    void createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType);
}
