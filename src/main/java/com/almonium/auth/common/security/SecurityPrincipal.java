package com.almonium.auth.common.security;

import com.almonium.auth.common.model.enums.AuthProviderType;
import java.io.Serializable;
import java.util.UUID;

public record SecurityPrincipal(UUID principalId, UUID userId, String email, AuthProviderType provider)
        implements Serializable {}
