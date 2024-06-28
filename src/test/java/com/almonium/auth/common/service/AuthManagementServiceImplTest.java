package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.exception.AuthMethodNotFoundException;
import com.almonium.auth.common.exception.LastAuthMethodException;
import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.common.service.impl.AuthManagementServiceImpl;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import com.almonium.auth.local.service.impl.SecureRandomTokenGeneratorImpl;
import com.almonium.infra.email.service.EmailComposerService;
import com.almonium.infra.email.service.EmailService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
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
class AuthManagementServiceImplTest {
    private static final int OTP_LENGTH = 6;

    @InjectMocks
    AuthManagementServiceImpl authService;

    @Mock
    UserService userService;

    @Mock
    PrincipalRepository principalRepository;

    @Mock
    PrincipalFactory passwordEncoder;

    @Mock
    EmailService emailService;

    @Mock
    EmailComposerService emailComposerService;

    @Mock
    SecureRandomTokenGeneratorImpl tokenGenerator;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @DisplayName("Should add local login successfully")
    @Test
    void givenValidLocalLoginRequest_whenLinkLocalAuth_thenSuccess() {
        // Arrange
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        User user = TestDataGenerator.buildTestUser();
        user.setEmail(localAuthRequest.email()); // Ensure email matches

        String token = "123456";
        when(tokenGenerator.generateOTP(OTP_LENGTH)).thenReturn(token);
        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);
        when(passwordEncoder.createLocalPrincipal(user, localAuthRequest))
                .thenReturn(new LocalPrincipal(user, localAuthRequest.email(), "encodedPassword"));
        when(emailComposerService.composeEmail(localAuthRequest.email(), token, TokenType.EMAIL_VERIFICATION))
                .thenReturn(TestDataGenerator.createEmailDto());

        // Act
        authService.linkLocalAuth(user.getId(), localAuthRequest);

        // Assert
        verify(userService).getUserWithPrincipals(user.getId());
        verify(emailService).sendEmail(eq(TestDataGenerator.createEmailDto()));
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(tokenGenerator).generateOTP(OTP_LENGTH);
        verify(passwordEncoder).createLocalPrincipal(user, localAuthRequest);
        verify(principalRepository).save(any(Principal.class));
    }

    @DisplayName("Should throw exception when email mismatch on adding local login")
    @Test
    void givenEmailMismatch_whenLinkLocalAuth_thenThrowEmailMismatchException() {
        // Arrange
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        User user = TestDataGenerator.buildTestUser();
        user.setEmail("different-email@example.com");

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.linkLocalAuth(user.getId(), localAuthRequest))
                .isInstanceOf(EmailMismatchException.class)
                .hasMessageContaining("You need to register with the email you currently use: " + user.getEmail());

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).save(any(Principal.class));
    }

    @DisplayName("Should throw exception when local login already exists")
    @Test
    void givenExistingLocalLogin_whenLinkLocalAuth_thenThrowUserAlreadyExistsAuthenticationException() {
        // Arrange
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        User user = TestDataGenerator.buildTestUser();
        Principal existingPrincipal = LocalPrincipal.builder()
                .user(user)
                .provider(AuthProviderType.LOCAL)
                .build();
        user.getPrincipals().add(existingPrincipal);

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.linkLocalAuth(user.getId(), localAuthRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("You already have local account registered with " + user.getEmail());

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).save(any(Principal.class));
    }

    @DisplayName("Should unlink provider successfully")
    @Test
    void givenValidProvider_whenUnlinkProvider_thenSuccess() {
        // Arrange
        User user = TestDataGenerator.buildTestUser();
        Principal principalGoogle = TestDataGenerator.buildTestPrincipal(AuthProviderType.GOOGLE);
        Principal principalFacebook = TestDataGenerator.buildTestPrincipal(AuthProviderType.FACEBOOK);
        user.getPrincipals().add(principalGoogle);
        user.getPrincipals().add(principalFacebook);

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act
        authService.unlinkAuthMethod(user.getId(), AuthProviderType.GOOGLE);

        // Assert
        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository).delete(principalGoogle);
    }

    @DisplayName("Should throw exception when provider not found")
    @Test
    void givenInvalidProvider_whenUnlinkProvider_thenThrowException() {
        // Arrange
        User user = TestDataGenerator.buildTestUser();

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.unlinkAuthMethod(user.getId(), AuthProviderType.GOOGLE))
                .isInstanceOf(AuthMethodNotFoundException.class)
                .hasMessageContaining("Auth method not found GOOGLE");

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).delete(any(Principal.class));
    }

    @DisplayName("Should throw exception when trying to unlink the last auth method")
    @Test
    void givenLastAuthMethod_whenUnlinkProvider_thenThrowLastAuthMethodException() {
        // Arrange
        User user = TestDataGenerator.buildTestUser();
        Principal principal = TestDataGenerator.buildTestPrincipal(AuthProviderType.LOCAL);
        user.getPrincipals().add(principal);

        when(userService.getUserWithPrincipals(user.getId())).thenReturn(user);

        // Act & Assert
        assertThatThrownBy(() -> authService.unlinkAuthMethod(user.getId(), AuthProviderType.LOCAL))
                .isInstanceOf(LastAuthMethodException.class)
                .hasMessageContaining("Cannot remove the last authentication method for the user: " + user.getEmail());

        verify(userService).getUserWithPrincipals(user.getId());
        verify(principalRepository, never()).delete(any(Principal.class));
    }

    @DisplayName("Should create and send verification token successfully")
    @Test
    void givenLocalPrincipal_whenCreateAndSendVerificationToken_thenSuccess() {
        // Arrange
        LocalPrincipal localPrincipal = TestDataGenerator.buildTestLocalPrincipal();
        String token = "123456";

        when(tokenGenerator.generateOTP(6)).thenReturn(token);
        when(emailComposerService.composeEmail(localPrincipal.getEmail(), token, TokenType.EMAIL_VERIFICATION))
                .thenReturn(TestDataGenerator.createEmailDto());

        // Act
        authService.createAndSendVerificationToken(localPrincipal, TokenType.EMAIL_VERIFICATION);

        // Assert
        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(emailService).sendEmail(eq(TestDataGenerator.createEmailDto()));
    }
}
