package com.almonium.auth.common.dto.response;

import com.almonium.auth.common.model.enums.AuthProviderType;
import java.time.Instant;
import lombok.Data;

@Data
public class PrincipalDto {
    private AuthProviderType provider;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
}
