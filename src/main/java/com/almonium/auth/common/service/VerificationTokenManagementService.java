package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.local.exception.InvalidVerificationTokenException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.TokenGenerator;
import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class VerificationTokenManagementService {
    private static final int COOLDOWN_SECONDS = 60;

    AuthTokenEmailComposerService emailComposerService;
    UserService userService;
    TokenGenerator tokenGenerator;
    VerificationTokenRepository verificationTokenRepository;
    LocalPrincipalRepository localPrincipalRepository;
    AppProperties appProperties;

    @Transactional
    public Optional<VerificationToken> findValidEmailVerificationToken(UUID userId) {
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

    public void createAndSendVerificationTokenIfAllowed(LocalPrincipal localPrincipal, TokenType tokenType) {
        String token = tokenGenerator.generateOTP(
                appProperties.getAuth().getVerificationToken().getLength());

        verificationTokenRepository
                .findByPrincipalAndTokenTypeIn(localPrincipal, Set.of(tokenType))
                .ifPresent(existingToken -> {
                    Instant cooldown = existingToken.getCreatedAt().plusSeconds(COOLDOWN_SECONDS);
                    Instant now = Instant.now();
                    if (now.isBefore(cooldown)) {
                        log.info("Verification token cooldown for {}", localPrincipal.getEmail());
                        Duration duration = Duration.between(now, cooldown);
                        throw new BadUserRequestActionException("You can request a new verification token in %s seconds"
                                .formatted(duration.getSeconds()));
                    }
                    verificationTokenRepository.delete(existingToken);
                    log.info("Deleted old verification token for {}", localPrincipal.getEmail());
                });

        VerificationToken verificationToken = new VerificationToken(
                localPrincipal,
                token,
                tokenType,
                appProperties.getAuth().getVerificationToken().getLifetime());
        verificationTokenRepository.save(verificationToken);

        var emailContext = new EmailContext<>(tokenType, Map.of(AuthTokenEmailComposerService.TOKEN_ATTRIBUTE, token));
        String username = localPrincipal.getUser().getUsername();
        emailComposerService.sendEmail(username, localPrincipal.getEmail(), emailContext);
        log.info("Verification token sent to {}", localPrincipal.getEmail());
    }

    public VerificationToken validateAndDeleteTokenOrThrow(String token, TokenType expectedType) {
        VerificationToken verificationToken = verifyOrThrow(token, expectedType);

        verificationTokenRepository.delete(verificationToken);
        return verificationToken;
    }

    public VerificationToken verifyOrThrow(String token, TokenType expectedType) {
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

    public void deleteToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    public boolean isTokenValid(String token, TokenType expectedType) {
        try {
            verifyOrThrow(token, expectedType);
            return true;
        } catch (InvalidVerificationTokenException e) {
            return false;
        }
    }
}
