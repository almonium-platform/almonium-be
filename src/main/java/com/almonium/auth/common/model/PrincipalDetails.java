package com.almonium.auth.common.model;

import com.almonium.auth.common.model.enums.AuthProviderType;
import java.util.UUID;

public interface PrincipalDetails {
    UUID getPrincipalId();

    UUID getUserId();

    String getEmail();

    AuthProviderType getProvider();
}
