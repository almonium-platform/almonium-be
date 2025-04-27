package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.events.VerificationEmailRequestedEvent;
import com.almonium.auth.local.exception.InvalidVerificationTokenException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.ApacheAlphanumericGeneratorImpl;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.service.UserService;
import com.almonium.util.TestDataGenerator;
import com.almonium.util.config.AppConfigPropertiesTest;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class VerificationTokenManagementServiceTest extends AppConfigPropertiesTest {
    private static final int COOLDOWN_SECONDS = 60;

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

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    void setUp() {
        verificationTokenManagementService = new VerificationTokenManagementService(
                userService,
                tokenGenerator,
                verificationTokenRepository,
                localPrincipalRepository,
                appProperties,
                applicationEventPublisher);
    }

    @DisplayName("Should create and send verification token successfully")
    @Test
    void givenLocalPrincipal_whenCreateAndSendVerificationToken_IfAllowed_thenSuccess() {
        // Arrange
        LocalPrincipal localPrincipal = TestDataGenerator.buildTestLocalPrincipal();
        String token = "1234567890abcd1234567890";

        when(tokenGenerator.generateOTP(anyInt())).thenReturn(token);
        VerificationToken existingToken =
                new VerificationToken(localPrincipal, token, TokenType.EMAIL_VERIFICATION, 10);
        existingToken.setCreatedAt(Instant.now().minusSeconds(COOLDOWN_SECONDS * 2));
        when(verificationTokenRepository.findByPrincipalAndTokenTypeIn(
                        localPrincipal, Set.of(TokenType.EMAIL_VERIFICATION)))
                .thenReturn(Optional.of(existingToken));

        // Simulate that no existing token exists or the cooldown has expired.
        when(verificationTokenRepository.findByPrincipalAndTokenTypeIn(
                        localPrincipal, Set.of(TokenType.EMAIL_VERIFICATION)))
                .thenReturn(Optional.empty()); // No existing token, or it's expired.

        // Act
        verificationTokenManagementService.createAndSendVerificationTokenIfAllowed(
                localPrincipal, TokenType.EMAIL_VERIFICATION);

        // Assert
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(applicationEventPublisher).publishEvent(any(VerificationEmailRequestedEvent.class));
    }

    @DisplayName("Should throw BadUserRequestActionException if cooldown period is not over")
    @Test
    void givenLocalPrincipal_whenCreateAndSendVerificationToken_IfCooldown_thenException() {
        // Arrange
        LocalPrincipal localPrincipal = TestDataGenerator.buildTestLocalPrincipal();
        String token = "1234567890abcd1234567890";

        when(tokenGenerator.generateOTP(anyInt())).thenReturn(token);

        // Simulate that there is an existing token, and the cooldown period is still in effect.
        VerificationToken existingToken =
                new VerificationToken(localPrincipal, token, TokenType.EMAIL_VERIFICATION, 10);
        existingToken.setCreatedAt(Instant.now().minusSeconds((long) (COOLDOWN_SECONDS * 0.5)));
        when(verificationTokenRepository.findByPrincipalAndTokenTypeIn(
                        localPrincipal, Set.of(TokenType.EMAIL_VERIFICATION)))
                .thenReturn(Optional.of(existingToken));

        // Act
        BadUserRequestActionException exception = catchThrowableOfType(
                BadUserRequestActionException.class,
                () -> verificationTokenManagementService.createAndSendVerificationTokenIfAllowed(
                        localPrincipal, TokenType.EMAIL_VERIFICATION));

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains("You can request a new verification token in");
        verify(verificationTokenRepository, times(0)).save(any(VerificationToken.class)); // Ensure token wasn't saved
        verify(emailComposerService, times(0))
                .sendEmail(anyString(), anyString(), any(EmailContext.class)); // Ensure email wasn't sent
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
