package linguarium.auth.common.service;

import linguarium.auth.common.enums.AuthProviderType;
import linguarium.auth.local.dto.request.LocalAuthRequest;

public interface AuthManagementService {
    void linkLocalAuth(Long userId, LocalAuthRequest localAuthRequest);

    void unlinkProviderAuth(Long userId, AuthProviderType providerType);
}
