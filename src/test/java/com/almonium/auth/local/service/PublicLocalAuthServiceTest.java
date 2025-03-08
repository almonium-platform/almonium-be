package com.almonium.auth.local.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.user.core.factory.UserFactory;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.util.TestDataGenerator;
import com.almonium.util.config.AppConfigPropertiesTest;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class PublicLocalAuthServiceTest extends AppConfigPropertiesTest {
    PublicLocalAuthService authService;

    // services
    @Mock
    VerificationTokenManagementService verificationTokenManagementService;

    // factories
    @Mock
    UserFactory userFactory;

    @Mock
    PrincipalFactory principalFactory;

    // repositories
    @Mock
    UserRepository userRepository;

    @Mock
    LocalPrincipalRepository localPrincipalRepository;

    @BeforeEach
    void setUp() {
        authService = new PublicLocalAuthService(
                verificationTokenManagementService,
                userFactory,
                principalFactory,
                userRepository,
                localPrincipalRepository);
    }

    @DisplayName("Should successfully register local user")
    @Test
    void givenValidLocalRequest_whenRegister_thenSaveUser() {
        // Arrange
        LocalAuthRequest registrationRequest = TestDataGenerator.createLocalAuthRequest();
        User user = User.builder().email(registrationRequest.email()).build();

        when(principalFactory.createLocalPrincipal(user, registrationRequest))
                .thenReturn(new LocalPrincipal(user, registrationRequest.email(), "encodedPassword"));
        when(userFactory.createUserWithDefaultPlan(registrationRequest.email(), false))
                .thenReturn(user);

        // Act
        authService.register(registrationRequest);

        // Assert
        verify(principalFactory).createLocalPrincipal(user, registrationRequest);
        verify(userFactory).createUserWithDefaultPlan(registrationRequest.email(), false);
        verify(localPrincipalRepository).save(any(LocalPrincipal.class));
        verify(verificationTokenManagementService)
                .createAndSendVerificationTokenIfAllowed(any(LocalPrincipal.class), eq(TokenType.EMAIL_VERIFICATION));
    }

    @DisplayName("Should throw an exception when trying to register user with existing email")
    @Test
    void givenExistingUserEmail_whenRegister_thenThrowUserAlreadyExistsAuthenticationException() {
        LocalAuthRequest registrationRequest = new LocalAuthRequest("johnwick@gmail.com", "password");

        when(userRepository.existsByEmail(registrationRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(UserAlreadyExistsException.class);

        verify(userRepository).existsByEmail(registrationRequest.email());
        verify(userRepository, never()).existsById(any(UUID.class));
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).flush();
    }

    @DisplayName("Should request password reset successfully")
    @Test
    void givenValidEmail_whenRequestPasswordReset_thenSendVerificationToken() {
        // Arrange
        String email = "test@example.com";
        LocalPrincipal principal = TestDataGenerator.buildTestLocalPrincipal();
        when(localPrincipalRepository.findByEmail(email)).thenReturn(Optional.of(principal));

        // Act
        authService.requestPasswordReset(email);

        // Assert
        verify(localPrincipalRepository).findByEmail(email);
        verify(verificationTokenManagementService)
                .createAndSendVerificationTokenIfAllowed(principal, TokenType.PASSWORD_RESET);
    }
}
