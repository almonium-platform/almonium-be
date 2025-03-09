package com.almonium.auth.local.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.user.core.factory.UserFactory;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PublicLocalAuthService {
    VerificationTokenManagementService verificationTokenManagementService;

    UserFactory userFactory;
    PrincipalFactory principalFactory;

    UserRepository userRepository;
    LocalPrincipalRepository localPrincipalRepository;

    public void register(LocalAuthRequest request) {
        validateRegisterRequest(request);
        User user = userFactory.createUserWithDefaultPlan(request.email(), false);
        LocalPrincipal localPrincipal = principalFactory.createLocalPrincipal(user, request);
        localPrincipalRepository.save(localPrincipal);
        verificationTokenManagementService.createAndSendVerificationTokenIfAllowed(
                localPrincipal, TokenType.EMAIL_VERIFICATION);
    }

    public void requestPasswordReset(String email) {
        localPrincipalRepository
                .findByEmail(email)
                .ifPresent(principal -> verificationTokenManagementService.createAndSendVerificationTokenIfAllowed(
                        principal, TokenType.PASSWORD_RESET));
    }

    private void validateRegisterRequest(LocalAuthRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }
    }
}
