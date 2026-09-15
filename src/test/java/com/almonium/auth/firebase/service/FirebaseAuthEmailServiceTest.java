package com.almonium.auth.firebase.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.enums.AuthEmailTemplateType;
import com.almonium.infra.email.service.AuthEmailComposerService;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FirebaseAuthEmailServiceTest {
    @Mock
    FirebaseAuthGateway firebaseAuthGateway;

    @Mock
    AuthEmailComposerService emailComposerService;

    FirebaseAuthEmailService service;

    @BeforeEach
    void setUp() {
        AppProperties properties = new AppProperties();
        properties.setWebDomain("https://almonium.com");
        service = new FirebaseAuthEmailService(firebaseAuthGateway, emailComposerService, properties);
    }

    @Test
    void sendsBrandedVerificationEmailWithCustomActionPageUrl() {
        when(firebaseAuthGateway.verifyIdToken("id-token", true))
                .thenReturn(new FirebaseIdentity("uid", "Learner@Example.com", false, Instant.now(), "password"));
        when(firebaseAuthGateway.generateEmailVerificationLink(
                        "learner@example.com", "https://almonium.com/verify-email"))
                .thenReturn("https://almonium.firebaseapp.com/__/auth/action?mode=verifyEmail&oobCode=one-time-code");

        service.sendEmailVerification("id-token");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<EmailContext<AuthEmailTemplateType>> context = ArgumentCaptor.forClass(EmailContext.class);
        verify(emailComposerService).sendEmail(eq("there"), eq("learner@example.com"), context.capture());
        org.assertj.core.api.Assertions.assertThat(context.getValue().templateType())
                .isEqualTo(AuthEmailTemplateType.EMAIL_VERIFICATION);
        org.assertj.core.api.Assertions.assertThat(context.getValue().attributes())
                .containsEntry(
                        AuthEmailComposerService.ACTION_URL, "https://almonium.com/verify-email?oobCode=one-time-code");
    }

    @Test
    void keepsPasswordResetRequestsIndistinguishableForUnknownEmails() {
        when(firebaseAuthGateway.generatePasswordResetLink(
                        "missing@example.com", "https://almonium.com/reset-password"))
                .thenReturn(Optional.empty());

        service.sendPasswordReset("Missing@example.com");

        verify(emailComposerService, never()).sendEmail(any(), any(), any());
    }

    @Test
    void sendsPasswordResetToCustomActionPage() {
        when(firebaseAuthGateway.generatePasswordResetLink(
                        "learner@example.com", "https://almonium.com/reset-password"))
                .thenReturn(Optional.of(
                        "https://almonium.firebaseapp.com/__/auth/action?mode=resetPassword&oobCode=one-time-code"));

        service.sendPasswordReset("learner@example.com");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<EmailContext<AuthEmailTemplateType>> context = ArgumentCaptor.forClass(EmailContext.class);
        verify(emailComposerService).sendEmail(eq("there"), eq("learner@example.com"), context.capture());
        org.assertj.core.api.Assertions.assertThat(context.getValue().templateType())
                .isEqualTo(AuthEmailTemplateType.PASSWORD_RESET);
        org.assertj.core.api.Assertions.assertThat(context.getValue().attributes())
                .containsEntry(
                        AuthEmailComposerService.ACTION_URL,
                        "https://almonium.com/reset-password?oobCode=one-time-code");
    }
}
