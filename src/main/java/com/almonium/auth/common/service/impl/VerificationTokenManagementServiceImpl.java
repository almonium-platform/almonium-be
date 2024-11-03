package com.almonium.auth.common.service.impl;

import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.local.exception.InvalidVerificationTokenException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.TokenGenerator;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationTokenManagementServiceImpl implements VerificationTokenManagementService {
    public static final int TOKEN_LIFESPAN = 60;
    private static final int OTP_LENGTH = 24;
    VerificationTokenRepository verificationTokenRepository;
    TokenGenerator tokenGenerator;
    AuthTokenEmailComposerService emailComposerService;
    EmailService emailService;
    UserService userService;
    LocalPrincipalRepository localPrincipalRepository;

    @Override
    public Optional<VerificationToken> findValidEmailVerificationToken(long userId) {
        User user = userService.getUserWithPrincipals(userId);

        return userService
                .getUnverifiedLocalPrincipal(user)
                .or(() -> userService.getLocalPrincipal(user))
                .flatMap(localPrincipal -> {
                    Optional<VerificationToken> token = verificationTokenRepository.findByPrincipalAndTokenTypeIn(
                            localPrincipal, Set.of(TokenType.EMAIL_VERIFICATION, TokenType.EMAIL_CHANGE_VERIFICATION));

                    if (token.isPresent() && token.get().getExpiresAt().isAfter(Instant.now())) {
                        return token;
                    }

                    if (token.isPresent()) {
                        verificationTokenRepository.delete(token.get());
                        localPrincipalRepository.delete(localPrincipal);
                    }
                    return Optional.empty();
                });
    }

    @Override
    public void createAndSendVerificationToken(LocalPrincipal localPrincipal, TokenType tokenType) {
        String token = tokenGenerator.generateOTP(OTP_LENGTH);
        VerificationToken verificationToken = new VerificationToken(localPrincipal, token, tokenType, TOKEN_LIFESPAN);
        verificationTokenRepository.save(verificationToken);
        EmailDto emailDto = emailComposerService.composeEmail(localPrincipal.getEmail(), tokenType, token);
        emailService.sendEmail(emailDto);
        log.info("Verification token sent to {}", localPrincipal.getEmail());
    }

    @Override
    public VerificationToken getValidTokenOrThrow(String token, TokenType expectedType) {
        VerificationToken verificationToken = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new InvalidVerificationTokenException("Token is invalid or has been used"));

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidVerificationTokenException("Verification token has expired");
        }

        if (verificationToken.getTokenType() != expectedType) {
            throw new InvalidVerificationTokenException("Invalid token type: should be " + expectedType + " but got "
                    + verificationToken.getTokenType() + " instead");
        }

        return verificationToken;
    }

    @Override
    public void deleteToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}
