package com.almonium.auth.local.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.service.UserAuthenticationService;
import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.response.JwtAuthResponse;
import com.almonium.auth.local.exception.EmailNotVerifiedException;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.service.PublicLocalAuthService;
import com.almonium.auth.token.dto.response.JwtTokenResponse;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PublicLocalAuthServiceImpl implements PublicLocalAuthService {
    // services
    AuthenticationManager authenticationManager;
    UserAuthenticationService userAuthenticationServiceImpl;
    VerificationTokenManagementService verificationTokenManagementService;
    UserService userService;
    PrincipalFactory principalFactory;
    // repositories
    UserRepository userRepository;
    LocalPrincipalRepository localPrincipalRepository;

    @NonFinal
    @Value("${app.auth.email-verification-required}")
    boolean emailVerificationRequired;

    @Override
    public JwtAuthResponse login(LocalAuthRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        LocalPrincipal localPrincipal = validateAndGetLocalPrincipal(request);

        JwtTokenResponse tokenResponse =
                userAuthenticationServiceImpl.authenticateUser(localPrincipal, response, authentication);

        return new JwtAuthResponse(
                tokenResponse.accessToken(),
                tokenResponse.refreshToken(),
                userService.buildUserInfoFromUser(localPrincipal.getUser()));
    }

    @Override
    public void register(LocalAuthRequest request) {
        validateRegisterRequest(request);
        User user = User.builder().email(request.email()).build();
        LocalPrincipal localPrincipal = principalFactory.createLocalPrincipal(user, request);
        userRepository.save(user);
        localPrincipalRepository.save(localPrincipal);
        verificationTokenManagementService.createAndSendVerificationToken(localPrincipal, TokenType.EMAIL_VERIFICATION);
    }

    @Override
    public void requestPasswordReset(String email) {
        localPrincipalRepository
                .findByEmail(email)
                .ifPresent(principal -> verificationTokenManagementService.createAndSendVerificationToken(
                        principal, TokenType.PASSWORD_RESET));
    }

    private LocalPrincipal validateAndGetLocalPrincipal(LocalAuthRequest request) {
        LocalPrincipal localPrincipal = localPrincipalRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("User not found " + request.email()));

        if (emailVerificationRequired && !localPrincipal.isEmailVerified()) {
            throw new EmailNotVerifiedException("Email needs to be verified before logging in.");
        }
        return localPrincipal;
    }

    private void validateRegisterRequest(LocalAuthRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }
    }
}
