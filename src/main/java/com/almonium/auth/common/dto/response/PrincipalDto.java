package com.almonium.auth.common.dto.response;

import com.almonium.auth.common.model.enums.AuthProviderType;
import java.time.Instant;

public record PrincipalDto(AuthProviderType provider, String email, Instant createdAt, Instant updatedAt) {}
