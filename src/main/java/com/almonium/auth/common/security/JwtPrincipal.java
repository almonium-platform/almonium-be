package com.almonium.auth.common.security;

import com.almonium.auth.common.model.PrincipalDetails;
import com.almonium.auth.common.model.enums.AuthProviderType;
import java.util.UUID;

public record JwtPrincipal(UUID principalId, UUID userId, String email, AuthProviderType provider)
        implements PrincipalDetails {
    @Override
    public UUID getPrincipalId() {
        return principalId;
    }

    @Override
    public UUID getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public AuthProviderType getProvider() {
        return provider;
    }
}
