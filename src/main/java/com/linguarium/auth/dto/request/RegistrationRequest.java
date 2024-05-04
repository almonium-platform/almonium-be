package com.linguarium.auth.dto.request;

import com.linguarium.auth.dto.SocialProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8, max = 32)
    private String password;
    @NotNull
    private SocialProvider socialProvider;
    private String profilePicLink;
    private String providerUserId;
}
