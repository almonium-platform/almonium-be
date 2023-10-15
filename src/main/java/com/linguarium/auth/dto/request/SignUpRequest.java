package com.linguarium.auth.dto.request;

import com.linguarium.auth.dto.SocialProvider;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private Long userID;
    private String providerUserId;
    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    private SocialProvider socialProvider;
    private String profilePicLink;
    @Size(min = 8, message = "{Size.userDto.password}")
    private String password;
}
