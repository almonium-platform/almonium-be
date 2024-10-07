package com.almonium.auth.common.service.impl;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.token.dto.response.JwtTokenResponse;
import com.almonium.auth.token.service.impl.AuthTokenService;
import com.almonium.user.core.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    AuthTokenService authTokenService;
    ProfileService profileService;

    public JwtTokenResponse authenticateUser(
            Principal principal, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        profileService.updateLoginStreak(principal.getUser().getProfile());
        return authTokenService.createAndSetAccessAndRefreshTokens(authentication, response);
    }
}
