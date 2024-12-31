package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.local.exception.InvalidVerificationTokenException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.ApacheAlphanumericGeneratorImpl;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.user.core.service.UserService;
import com.almonium.util.TestDataGenerator;
import com.almonium.util.config.AppConfigPropertiesTest;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class VerificationTokenManagementServiceTest extends AppConfigPropertiesTest {

    @InjectMocks
    VerificationTokenManagementService verificationTokenManagementService;

    @Mock
    EmailService emailService;

    @Mock
    UserService userService;

    @Mock
    AuthTokenEmailComposerService emailComposerService;

    @Mock
    ApacheAlphanumericGeneratorImpl tokenGenerator;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @Mock
    LocalPrincipalRepository localPrincipalRepository;

    @BeforeEach
    void setUp() {
        verificationTokenManagementService = new VerificationTokenManagementService(
                emailService,
                emailComposerService,
                userService,
                tokenGenerator,
                verificationTokenRepository,
                localPrincipalRepository,
                appProperties);
    }

    @DisplayName("Should create and send verification token successfully")
    @Test
    void givenLocalPrincipal_whenCreateAndSendVerificationToken_thenSuccess() {
        // Arrange
        LocalPrincipal localPrincipal = TestDataGenerator.buildTestLocalPrincipal();
        String token = "1234567890abcd1234567890";
        EmailDto emailDto = TestDataGenerator.createEmailDto();

        when(tokenGenerator.generateOTP(anyInt())).thenReturn(token);
        var emailContext = new EmailContext<>(
                TokenType.EMAIL_VERIFICATION, Map.of(AuthTokenEmailComposerService.TOKEN_ATTRIBUTE, token));
        when(emailComposerService.composeEmail(localPrincipal.getEmail(), emailContext))
                .thenReturn(emailDto);

        // Act
        verificationTokenManagementService.createAndSendVerificationToken(localPrincipal, TokenType.EMAIL_VERIFICATION);

        // Assert
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(emailService).sendEmail(eq(emailDto));
    }

    @DisplayName("Should throw exception when token is expired")
    @Test
    void givenExpiredToken_whenGetTokenOrThrow_thenThrowInvalidVerificationValidTokenException() {
        // Arrange
        String token = "expiredToken";
        LocalPrincipal principal = TestDataGenerator.buildTestLocalPrincipal();
        VerificationToken verificationToken = new VerificationToken(principal, token, TokenType.EMAIL_VERIFICATION, 60);
        verificationToken.setExpiresAt(Instant.now().minusSeconds(100)); // Set token to expired

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act & Assert
        assertThatThrownBy(() -> verificationTokenManagementService.validateAndDeleteTokenOrThrow(
                token, TokenType.EMAIL_VERIFICATION))
                .isInstanceOf(InvalidVerificationTokenException.class)
                .hasMessage("Verification token has expired");

        verify(verificationTokenRepository).findByToken(token);
    }

    @DisplayName("Should throw exception when token is invalid")
    @Test
    void givenInvalidToken_whenGetTokenOrThrow_thenThrowInvalidVerificationValidTokenException() {
        // Arrange
        String token = "invalidToken";

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> verificationTokenManagementService.validateAndDeleteTokenOrThrow(
                token, TokenType.EMAIL_VERIFICATION))
                .isInstanceOf(InvalidVerificationTokenException.class)
                .hasMessage("Token is invalid or has been used");

        verify(verificationTokenRepository).findByToken(token);
    }

    @DisplayName("Should throw exception when token type does not match expected type")
    @Test
    void givenTokenTypeMismatch_whenGetTokenOrThrow_thenThrowInvalidVerificationValidTokenException() {
        // Arrange
        String token = "validToken";
        LocalPrincipal principal = TestDataGenerator.buildTestLocalPrincipal();
        VerificationToken verificationToken = new VerificationToken(principal, token, TokenType.PASSWORD_RESET, 60);

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act & Assert
        assertThatThrownBy(() -> verificationTokenManagementService.validateAndDeleteTokenOrThrow(
                token, TokenType.EMAIL_VERIFICATION))
                .isInstanceOf(InvalidVerificationTokenException.class)
                .hasMessage("Invalid token type: should be EMAIL_VERIFICATION but got PASSWORD_RESET instead");

        verify(verificationTokenRepository).findByToken(token);
    }

    @DisplayName("Should delete the verification token successfully")
    @Test
    void givenVerificationToken_whenDeleteToken_thenSuccess() {
        // Arrange
        VerificationToken verificationToken = new VerificationToken(
                TestDataGenerator.buildTestLocalPrincipal(), "validToken", TokenType.EMAIL_VERIFICATION, 60);

        // Act
        verificationTokenManagementService.deleteToken(verificationToken);

        // Assert
        verify(verificationTokenRepository).delete(verificationToken);
    }

    @DisplayName("Should return verification token when token is valid and type matches expected type")
    @Test
    void givenValidTokenAndMatchingType_whenGetTokenOrThrow_thenReturnVerificationValidToken() {
        // Arrange
        String token = "validToken";
        LocalPrincipal principal = TestDataGenerator.buildTestLocalPrincipal();
        VerificationToken verificationToken = new VerificationToken(principal, token, TokenType.EMAIL_VERIFICATION, 60);
        verificationToken.setExpiresAt(Instant.now().plusSeconds(100)); // Set token to not expired

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act
        VerificationToken result =
                verificationTokenManagementService.validateAndDeleteTokenOrThrow(token, TokenType.EMAIL_VERIFICATION);

        // Assert
        verify(verificationTokenRepository).findByToken(token);
        verify(verificationTokenRepository).delete(any(VerificationToken.class));
        assertThat(result).isEqualTo(verificationToken);
    }
}
