package com.almonium.auth.local.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.factory.PrincipalFactory;
import com.almonium.auth.common.service.VerificationTokenManagementService;
import com.almonium.auth.common.service.impl.UserAuthenticationServiceImpl;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.dto.response.JwtAuthResponse;
import com.almonium.auth.local.exception.EmailNotVerifiedException;
import com.almonium.auth.local.exception.UserAlreadyExistsException;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.LocalPrincipalRepository;
import com.almonium.auth.local.service.impl.PublicLocalAuthServiceImpl;
import com.almonium.auth.token.dto.response.JwtTokenResponse;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UserService;
import com.almonium.user.core.service.impl.UserUtility;
import com.almonium.util.TestDataGenerator;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
@ActiveProfiles("test")
@TestPropertySource(properties = {"app.auth.email-verification-required=true"})
class PublicLocalAuthServiceImplTest {
    private static final String IS_EMAIL_VERIFICATION_REQUIRED_FIELD = "emailVerificationRequired";

    @InjectMocks
    PublicLocalAuthServiceImpl authService;

    @Mock
    UserRepository userRepository;

    @Mock
    LocalPrincipalRepository localPrincipalRepository;

    @Mock
    PrincipalFactory principalFactory;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    VerificationTokenManagementService verificationTokenManagementService;

    @Mock
    UserService userService;

    @Mock
    UserAuthenticationServiceImpl userAuthenticationServiceImpl;

    @DisplayName("Should successfully register local user")
    @Test
    void givenValidLocalRequest_whenRegister_thenSaveUser() {
        // Arrange
        LocalAuthRequest registrationRequest = TestDataGenerator.createLocalAuthRequest();
        User user = User.builder().email(registrationRequest.email()).build();

        when(principalFactory.createLocalPrincipal(user, registrationRequest))
                .thenReturn(new LocalPrincipal(user, registrationRequest.email(), "encodedPassword"));

        // Act
        authService.register(registrationRequest);

        // Assert
        verify(principalFactory).createLocalPrincipal(user, registrationRequest);
        verify(userRepository).save(user);
        verify(localPrincipalRepository).save(any(LocalPrincipal.class));
        verify(verificationTokenManagementService)
                .createAndSendVerificationToken(any(LocalPrincipal.class), eq(TokenType.EMAIL_VERIFICATION));
    }

    @DisplayName("Should authenticate and return JWT when given valid credentials")
    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnJwtAndUserInfo() {
        // Arrange
        User user = UserUtility.getUser();

        String email = user.getEmail();
        String password = "fdsfsd";
        String expectedRefreshJwt = "xxx.yyy.zzz";
        String expectedAccessJwt = "aaa.bbb.ccc";
        LocalAuthRequest localAuthRequest = new LocalAuthRequest(email, password);
        LocalPrincipal principal =
                LocalPrincipal.builder().user(user).emailVerified(true).build();
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(localPrincipalRepository.findByEmail(email)).thenReturn(Optional.of(principal));
        when(userAuthenticationServiceImpl.authenticateUser(
                        eq(principal), any(HttpServletResponse.class), any(Authentication.class)))
                .thenReturn(new JwtTokenResponse(expectedAccessJwt, expectedRefreshJwt));
        // Act
        JwtAuthResponse result = authService.login(localAuthRequest, mock(HttpServletResponse.class));

        // Assert
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(userAuthenticationServiceImpl)
                .authenticateUser(any(LocalPrincipal.class), any(HttpServletResponse.class), any(Authentication.class));
        assertThat(result.accessToken()).isEqualTo(expectedAccessJwt);
        assertThat(result.refreshToken()).isEqualTo(expectedRefreshJwt);
    }

    @DisplayName("Should throw an exception when trying to register user with existing email")
    @Test
    void givenExistingUserEmail_whenRegister_thenThrowUserAlreadyExistsAuthenticationException() {
        LocalAuthRequest registrationRequest = new LocalAuthRequest("johnwick@gmail.com", "password");

        when(userRepository.existsByEmail(registrationRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(UserAlreadyExistsException.class);

        verify(userRepository).existsByEmail(registrationRequest.email());
        verify(userRepository, never()).existsById(anyLong());
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).flush();
    }

    @DisplayName("Should throw an exception when email is not verified during login")
    @Test
    void givenUnverifiedEmail_whenLogin_thenThrowEmailNotVerifiedException() {
        // Arrange
        LocalAuthRequest localAuthRequest = TestDataGenerator.createLocalAuthRequest();
        LocalPrincipal principal = LocalPrincipal.builder()
                .email(localAuthRequest.email())
                .password("encodedPassword")
                .emailVerified(false)
                .build();
        when(localPrincipalRepository.findByEmail(localAuthRequest.email())).thenReturn(Optional.of(principal));
        ReflectionTestUtils.setField(authService, IS_EMAIL_VERIFICATION_REQUIRED_FIELD, true);

        // Act & Assert
        assertThatThrownBy(() -> authService.login(localAuthRequest, mock(HttpServletResponse.class)))
                .isInstanceOf(EmailNotVerifiedException.class)
                .hasMessage("Email needs to be verified before logging in.");

        verify(userAuthenticationServiceImpl, never())
                .authenticateUser(any(LocalPrincipal.class), any(HttpServletResponse.class), any(Authentication.class));
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
        verify(verificationTokenManagementService).createAndSendVerificationToken(principal, TokenType.PASSWORD_RESET);
    }
}
