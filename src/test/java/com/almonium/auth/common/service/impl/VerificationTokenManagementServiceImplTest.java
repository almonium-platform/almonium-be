package com.almonium.auth.common.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.impl.SecureRandomTokenGeneratorImpl;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.util.TestDataGenerator;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class VerificationTokenManagementServiceImplTest {

    @InjectMocks
    VerificationTokenManagementServiceImpl verificationTokenManagementService;

    @Mock
    EmailService emailService;

    @Mock
    AuthTokenEmailComposerService emailComposerService;

    @Mock
    SecureRandomTokenGeneratorImpl tokenGenerator;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @DisplayName("Should create and send verification token successfully")
    @Test
    void givenLocalPrincipal_whenCreateAndSendVerificationToken_thenSuccess() {
        // Arrange
        LocalPrincipal localPrincipal = TestDataGenerator.buildTestLocalPrincipal();
        String token = "123456";

        when(tokenGenerator.generateOTP(6)).thenReturn(token);
        when(emailComposerService.composeEmail(localPrincipal.getEmail(), TokenType.EMAIL_VERIFICATION, token))
                .thenReturn(TestDataGenerator.createEmailDto());

        // Act
        verificationTokenManagementService.createAndSendVerificationToken(localPrincipal, TokenType.EMAIL_VERIFICATION);

        // Assert
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(emailService).sendEmail(eq(TestDataGenerator.createEmailDto()));
    }
}
