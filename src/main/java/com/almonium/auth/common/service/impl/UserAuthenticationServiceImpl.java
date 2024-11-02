package com.almonium.auth.common.service.impl;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.service.UserAuthenticationService;
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

/**
 * Common entry point for user authentication across all live authentication methods.
 * This service sets up the security context, updates the user's login streak,
 * and attaches the access and refresh tokens to the response.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    AuthTokenService authTokenService;
    ProfileService profileService;

    @Override
    public JwtTokenResponse authenticateUser(
            Principal principal, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        profileService.updateLoginStreak(principal.getUser().getProfile());
        String accessToken = authTokenService.createAndSetAccessTokenForLiveLogin(authentication, response);
        String refreshToken = authTokenService.createAndSetRefreshToken(authentication, response);
        return new JwtTokenResponse(accessToken, refreshToken);
    }
}
