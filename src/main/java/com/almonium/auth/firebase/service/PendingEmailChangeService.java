package com.almonium.auth.firebase.service;

import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.PendingEmailChange;
import com.almonium.auth.firebase.repository.PendingEmailChangeRepository;
import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.enums.AuthEmailTemplateType;
import com.almonium.infra.email.service.AuthEmailComposerService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class PendingEmailChangeService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long LIFETIME_SECONDS = 60 * 60;

    private final PendingEmailChangeRepository pendingEmailChangeRepository;
    private final UserRepository userRepository;
    private final FirebaseAuthGateway firebaseAuthGateway;
    private final AuthEmailComposerService emailComposerService;
    private final AppProperties appProperties;

    @Transactional
    public void request(UUID userId, String requestedEmail) {
        User user = userRepository.findById(userId).orElseThrow();
        String email = requestedEmail.trim().toLowerCase(Locale.ROOT);
        if (email.equalsIgnoreCase(user.getEmail())) {
            throw new BadUserRequestActionException("New email must differ from your current email");
        }

        String token = nextToken();
        pendingEmailChangeRepository.save(
                new PendingEmailChange(userId, email, hash(token), Instant.now().plusSeconds(LIFETIME_SECONDS)));
        String url = UriComponentsBuilder.fromUriString(appProperties.getWebDomain() + "/change-email")
                .queryParam("token", token)
                .build()
                .encode()
                .toUriString();
        emailComposerService.sendEmail(
                user.getUsername() == null ? "there" : user.getUsername(),
                email,
                new EmailContext<>(
                        AuthEmailTemplateType.EMAIL_CHANGE, Map.of(AuthEmailComposerService.ACTION_URL, url)));
    }

    @Transactional
    public void confirm(String token) {
        PendingEmailChange pending = pendingEmailChangeRepository
                .findByTokenHash(hash(token))
                .orElseThrow(() ->
                        new BadUserRequestActionException("Email change link is invalid or has already been used"));
        if (pending.getExpiresAt().isBefore(Instant.now())) {
            pendingEmailChangeRepository.delete(pending);
            throw new BadUserRequestActionException("Email change link has expired");
        }
        User user = userRepository.findById(pending.getUserId()).orElseThrow();
        firebaseAuthGateway.updateEmail(user.getFirebaseUid(), pending.getEmail());
        user.setEmail(pending.getEmail());
        user.setEmailVerified(true);
        userRepository.save(user);
        pendingEmailChangeRepository.delete(pending);
    }

    private String nextToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(
                            MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
