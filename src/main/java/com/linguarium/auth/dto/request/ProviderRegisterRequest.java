package com.linguarium.auth.dto.request;

import com.linguarium.auth.dto.AuthProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRegisterRequest extends RegisterRequest {
    @NotNull
    private AuthProvider provider;

    @NotBlank
    private String providerUserId;

    private String avatarUrl;
}
