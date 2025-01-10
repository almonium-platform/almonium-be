package com.almonium.auth.common.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.local.exception.EmailNotVerifiedException;
import com.almonium.auth.token.service.AuthTokenService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.ProfileService;
import com.almonium.util.TestDataGenerator;
import com.almonium.util.config.AppConfigPropertiesTest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class AuthenticationServiceTest extends AppConfigPropertiesTest {
    AuthenticationService authenticationService;

    @Mock
    AuthTokenService authTokenService;

    @Mock
    ProfileService profileService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Mock
    HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(
                authTokenService, profileService, authenticationManager, userRepository, appProperties);
    }

    @DisplayName("Should authenticate and update tokens when given valid credentials")
    @Test
    void givenValidCredentials_whenAuthenticate_thenAuthenticateUserAndUpdateTokens() {
        // Arrange
        User user = TestDataGenerator.buildTestUserWithId();
        String email = user.getEmail();
        String password = "qwerty123";
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        appProperties.getAuth().setEmailVerificationRequired(false);

        // Act
        authenticationService.localLogin(email, password, mockResponse);

        // Assert
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(userRepository).findByEmail(email);
        verify(profileService).updateLoginStreak(user.getProfile());
        verify(authTokenService).createAndSetAccessTokenForLiveLogin(auth, mockResponse);
        verify(authTokenService).createAndSetRefreshToken(auth, mockResponse);
    }

    @DisplayName("Should throw an exception when email is not verified during login")
    @Test
    void givenUnverifiedEmail_whenLogin_thenThrowEmailNotVerifiedException() {
        // Arrange
        String email = "test@example.com";
        String password = "qwerty123";
        User user = User.builder().email(email).emailVerified(false).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> authenticationService.localLogin(email, password, mockResponse))
                .isInstanceOf(EmailNotVerifiedException.class)
                .hasMessage("Email needs to be verified before logging in.");

        verify(profileService, never()).updateLoginStreak(any());
        verify(authTokenService, never()).createAndSetAccessTokenForLiveLogin(any(), any());
        verify(authTokenService, never()).createAndSetRefreshToken(any(), any());
    }
}
