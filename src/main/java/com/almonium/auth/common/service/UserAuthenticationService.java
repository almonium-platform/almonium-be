package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.exception.EmailNotVerifiedException;
import com.almonium.auth.token.service.AuthTokenService;
import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class UserAuthenticationService {
    AuthTokenService authTokenService;
    ProfileService profileService;
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    AppProperties appProperties;

    public void localLogin(String email, String password, HttpServletResponse response) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        User user = validateAndGetLocalPrincipal(email);

        authenticateUser(user, response, authentication);
    }

    public void authenticateUser(User user, HttpServletResponse response, Authentication authentication) {
        profileService.updateLoginStreak(user.getProfile());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        authTokenService.createAndSetAccessTokenForLiveLogin(authentication, response);
        authTokenService.createAndSetRefreshToken(authentication, response);
    }

    private User validateAndGetLocalPrincipal(String email) {
        // loadUserByUsername is executed prior to this, thus IllegalState - it should always return a user
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User with email " + email + " not found"));

        if (appProperties.getAuth().isEmailVerificationRequired() && !user.isEmailVerified()) {
            throw new EmailNotVerifiedException("Email needs to be verified before logging in.");
        }
        return user;
    }
}
