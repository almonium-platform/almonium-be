package com.almonium.auth.common.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.service.UserAuthenticationService;
import com.almonium.auth.token.dto.response.JwtTokenResponse;
import com.almonium.auth.token.service.impl.AuthTokenService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Unified Authentication Entry Point for handling user authentication across all live methods,
 * including OAuth2, local login, and one-tap.
 *
 * <p>This service establishes the security context, updates the user's login streak, and
 * generates access and refresh tokens, attaching them to the HTTP response.
 * It provides a unified entry point for various authentication flows,
 * ensuring consistent handling and token issuance.</p>
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    AuthTokenService authTokenService;
    ProfileService profileService;

    @Override
    public JwtTokenResponse authenticateUser(User user, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        profileService.updateLoginStreak(user.getProfile());
        String accessToken = authTokenService.createAndSetAccessTokenForLiveLogin(authentication, response);
        String refreshToken = authTokenService.createAndSetRefreshToken(authentication, response);
        return new JwtTokenResponse(accessToken, refreshToken);
    }
}
