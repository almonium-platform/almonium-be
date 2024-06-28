package com.almonium.auth.common.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.exception.AuthMethodNotFoundException;
import com.almonium.auth.common.exception.LastAuthMethodException;
import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.common.service.AuthManagementService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.TokenGenerator;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.EmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
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
public class AuthManagementServiceImpl implements AuthManagementService {
    private static final int OTP_LENGTH = 6;
    EmailService emailService;
    EmailComposerService emailComposerService;
    UserService userService;
    PrincipalFactory principalFactory;
    PrincipalRepository principalRepository;
    VerificationTokenRepository verificationTokenRepository;
    TokenGenerator tokenGenerator;

    @Override
    public void linkLocalAuth(Long userId, LocalAuthRequest localAuthRequest) {
        User user = userService.getUserWithPrincipals(userId);
        validateAddLocalAuthRequest(user, localAuthRequest);
        LocalPrincipal localPrincipal = principalFactory.createLocalPrincipal(user, localAuthRequest);
        principalRepository.save(localPrincipal);
        createAndSendVerificationToken(localPrincipal, TokenType.EMAIL_VERIFICATION);
        log.info("Local auth for user {} waiting for verification", userId);
    }

    @Override
    public void unlinkAuthMethod(Long userId, AuthProviderType providerType) {
        User user = userService.getUserWithPrincipals(userId);
        Principal principal = getProviderIfPossibleElseThrow(providerType, user);
        user.getPrincipals().remove(principal);
        principalRepository.delete(principal);
        log.info("Provider: {} unlinked for user: {}", providerType, userId);
    }

    @Override
    public void createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType) {
        String token = tokenGenerator.generateOTP(OTP_LENGTH);
        VerificationToken verificationToken = new VerificationToken(localPrincipal, token, tokenType, 60);
        verificationTokenRepository.save(verificationToken);
        EmailDto emailDto = emailComposerService.composeEmail(localPrincipal.getEmail(), token, tokenType);
        emailService.sendEmail(emailDto);
    }

    private Principal getProviderIfPossibleElseThrow(AuthProviderType providerType, User user) {
        if (user.getPrincipals().size() == 1) {
            throw new LastAuthMethodException(
                    "Cannot remove the last authentication method for the user: " + user.getEmail());
        }

        return user.getPrincipals().stream()
                .filter(principal -> principal.getProvider() == providerType)
                .findFirst()
                .orElseThrow(() -> new AuthMethodNotFoundException("Auth method not found " + providerType));
    }

    private void validateAddLocalAuthRequest(User user, LocalAuthRequest request) {
        if (user.getPrincipals().stream()
                .anyMatch(principal -> principal.getProvider().equals(AuthProviderType.LOCAL))) {
            throw new UserAlreadyExistsException("You already have local account registered with " + user.getEmail());
        }
        if (!user.getEmail().equals(request.email())) {
            throw new EmailMismatchException(
                    "You need to register with the email you currently use: " + user.getEmail());
        }
    }
}
