package com.almonium.auth.common.service;

import com.almonium.auth.common.enums.AuthProviderType;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.enums.TokenType;

public interface AuthManagementService {
    void linkLocalAuth(Long userId, LocalAuthRequest localAuthRequest);

    void unlinkAuthMethod(Long userId, AuthProviderType providerType);

    void createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType);
}
