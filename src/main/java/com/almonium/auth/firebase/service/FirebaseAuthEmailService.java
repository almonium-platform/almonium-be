package com.almonium.auth.firebase.service;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.enums.AuthEmailTemplateType;
import com.almonium.infra.email.service.AuthEmailComposerService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FirebaseAuthEmailService {
    private final FirebaseAuthGateway firebaseAuthGateway;
    private final AuthEmailComposerService emailComposerService;
    private final AppProperties appProperties;

    public FirebaseAuthEmailService(
            FirebaseAuthGateway firebaseAuthGateway,
            AuthEmailComposerService emailComposerService,
            AppProperties appProperties) {
        this.firebaseAuthGateway = firebaseAuthGateway;
        this.emailComposerService = emailComposerService;
        this.appProperties = appProperties;
    }

    public void sendEmailVerification(String idToken) {
        FirebaseIdentity identity = firebaseAuthGateway.verifyIdToken(idToken, true);
        if (identity.emailVerified()) {
            return;
        }
        String email = requiredEmail(identity);
        String destination = actionUrl(
                firebaseAuthGateway.generateEmailVerificationLink(
                        email, appProperties.getWebDomain() + "/verify-email"),
                "/verify-email");
        send(email, AuthEmailTemplateType.EMAIL_VERIFICATION, destination);
    }

    public void sendPasswordReset(String email) {
        String normalizedEmail = email.trim().toLowerCase(java.util.Locale.ROOT);
        firebaseAuthGateway
                .generatePasswordResetLink(normalizedEmail, appProperties.getWebDomain() + "/reset-password")
                .map(link -> actionUrl(link, "/reset-password"))
                .ifPresent(destination -> send(normalizedEmail, AuthEmailTemplateType.PASSWORD_RESET, destination));
    }

    private void send(String email, AuthEmailTemplateType type, String actionUrl) {
        emailComposerService.sendEmail(
                "there", email, new EmailContext<>(type, Map.of(AuthEmailComposerService.ACTION_URL, actionUrl)));
    }

    private String requiredEmail(FirebaseIdentity identity) {
        if (identity.email() == null || identity.email().isBlank()) {
            throw new FirebaseAuthenticationException("Firebase account does not provide an email address");
        }
        return identity.email().trim().toLowerCase(java.util.Locale.ROOT);
    }

    private String actionUrl(String generatedLink, String path) {
        try {
            String code = UriComponentsBuilder.fromUri(new URI(generatedLink))
                    .build()
                    .getQueryParams()
                    .getFirst("oobCode");
            if (code == null || code.isBlank()) {
                throw new FirebaseAuthenticationException("Firebase action link did not include a one-time code");
            }
            return UriComponentsBuilder.fromUriString(appProperties.getWebDomain() + path)
                    .queryParam("oobCode", code)
                    .build()
                    .encode()
                    .toUriString();
        } catch (URISyntaxException | IllegalArgumentException exception) {
            throw new FirebaseAuthenticationException("Firebase returned an invalid action link", exception);
        }
    }
}
