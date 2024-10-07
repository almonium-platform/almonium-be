package com.almonium.auth.common.service;

import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.local.dto.request.LocalAuthRequest;

public interface AuthMethodManagementService {
    void linkLocalAuth(Long userId, LocalAuthRequest localAuthRequest);

    void unlinkAuthMethod(Long userId, AuthProviderType providerType);
}
