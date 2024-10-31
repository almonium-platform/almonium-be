package com.almonium.auth.common.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.service.impl.VerificationTokenManagementServiceImpl;
import com.almonium.auth.local.exception.InvalidVerificationTokenException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.impl.SecureRandomTokenGeneratorImpl;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.util.TestDataGenerator;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
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
        EmailDto emailDto = TestDataGenerator.createEmailDto();

        when(tokenGenerator.generateOTP(6)).thenReturn(token);
        when(emailComposerService.composeEmail(localPrincipal.getEmail(), TokenType.EMAIL_VERIFICATION, token))
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
        verificationToken.setExpiryDate(LocalDateTime.now().minusDays(1)); // Set token to expired

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act & Assert
        assertThatThrownBy(() ->
                        verificationTokenManagementService.getValidTokenOrThrow(token, TokenType.EMAIL_VERIFICATION))
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
        assertThatThrownBy(() ->
                        verificationTokenManagementService.getValidTokenOrThrow(token, TokenType.EMAIL_VERIFICATION))
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
        assertThatThrownBy(() ->
                        verificationTokenManagementService.getValidTokenOrThrow(token, TokenType.EMAIL_VERIFICATION))
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
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1)); // Set token to not expired

        when(verificationTokenRepository.findByToken(token)).thenReturn(Optional.of(verificationToken));

        // Act
        VerificationToken result =
                verificationTokenManagementService.getValidTokenOrThrow(token, TokenType.EMAIL_VERIFICATION);

        // Assert
        verify(verificationTokenRepository).findByToken(token);
        assertThat(result).isEqualTo(verificationToken);
    }
}
