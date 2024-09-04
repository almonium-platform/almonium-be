package com.almonium.auth.local.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.service.AuthManagementService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.response.JwtAuthResponse;
import com.almonium.auth.local.exception.EmailNotFoundException;
import com.almonium.auth.local.exception.EmailNotVerifiedException;
import com.almonium.auth.local.exception.InvalidVerificationTokenException;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.LocalAuthService;
import com.almonium.auth.token.service.impl.AuthTokenService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.ProfileService;
import com.almonium.user.core.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LocalAuthServiceImpl implements LocalAuthService {
    AuthManagementService authManagementService;
    UserService userService;
    UserRepository userRepository;
    ProfileService profileService;
    AuthTokenService authTokenService;
    PrincipalFactory principalFactory;
    AuthenticationManager manager;
    VerificationTokenRepository verificationTokenRepository;
    LocalPrincipalRepository localPrincipalRepository;

    @Override
    public JwtAuthResponse login(LocalAuthRequest request, HttpServletResponse response) {
        Authentication authentication =
                manager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        LocalPrincipal localPrincipal = localPrincipalRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("User not found " + request.email()));
        if (!localPrincipal.isEmailVerified()) {
            throw new EmailNotVerifiedException("Email needs to be verified before logging in.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = localPrincipal.getUser();
        authTokenService.revokeRefreshTokensByUser(user);
        profileService.updateLoginStreak(user.getProfile());

        String accessToken = authTokenService.createAndSetAccessToken(authentication, response);
        String refreshToken = authTokenService.createAndSetRefreshToken(authentication, response);
        return new JwtAuthResponse(accessToken, refreshToken, userService.buildUserInfoFromUser(user));
    }

    @Override
    public void register(LocalAuthRequest request) {
        validateRegisterRequest(request);
        User user = User.builder().email(request.email()).build();
        LocalPrincipal localPrincipal = principalFactory.createLocalPrincipal(user, request);
        userRepository.save(user);
        localPrincipalRepository.save(localPrincipal);
        authManagementService.createAndSendVerificationToken(localPrincipal, TokenType.EMAIL_VERIFICATION);
    }

    @Override
    public void verifyEmail(String token) {
        VerificationToken verificationToken = getTokenOrThrow(token, TokenType.EMAIL_VERIFICATION);
        LocalPrincipal principal = verificationToken.getPrincipal();
        principal.setEmailVerified(true);
        localPrincipalRepository.save(principal);
        verificationTokenRepository.delete(verificationToken);
    }

    @Override
    public void requestPasswordReset(String email) {
        LocalPrincipal localPrincipal = localPrincipalRepository
                .findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("Invalid email " + email));
        authManagementService.createAndSendVerificationToken(localPrincipal, TokenType.PASSWORD_RESET);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        VerificationToken verificationToken = getTokenOrThrow(token, TokenType.PASSWORD_RESET);
        LocalPrincipal principal = verificationToken.getPrincipal();
        principal.setPassword(principalFactory.encodePassword(newPassword));
        localPrincipalRepository.save(principal);
        verificationTokenRepository.delete(verificationToken);
    }

    private VerificationToken getTokenOrThrow(String token, TokenType expectedType) {
        VerificationToken verificationToken = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new InvalidVerificationTokenException("Invalid verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            throw new InvalidVerificationTokenException("Verification token has expired");
        }

        if (verificationToken.getTokenType() != expectedType) {
            throw new InvalidVerificationTokenException("Invalid token type: should be " + expectedType + " but got "
                    + verificationToken.getTokenType() + " instead");
        }

        return verificationToken;
    }

    private void validateRegisterRequest(LocalAuthRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }
    }
}
