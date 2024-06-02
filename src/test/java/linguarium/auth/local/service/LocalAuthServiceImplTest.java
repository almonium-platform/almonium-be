package linguarium.auth.local.service;

import static linguarium.user.core.service.impl.UserUtility.getUser;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import linguarium.auth.common.factory.PrincipalFactory;
import linguarium.auth.common.service.AuthManagementService;
import linguarium.auth.local.dto.request.LocalAuthRequest;
import linguarium.auth.local.dto.response.JwtAuthResponse;
import linguarium.auth.local.exception.UserAlreadyExistsException;
import linguarium.auth.local.model.entity.LocalPrincipal;
import linguarium.auth.local.repository.LocalPrincipalRepository;
import linguarium.auth.local.service.impl.LocalAuthServiceImpl;
import linguarium.config.security.jwt.TokenProvider;
import linguarium.user.core.model.entity.Profile;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.repository.UserRepository;
import linguarium.user.core.service.ProfileService;
import linguarium.user.core.service.UserService;
import linguarium.util.TestDataGenerator;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class LocalAuthServiceImplTest {
    @InjectMocks
    LocalAuthServiceImpl authService;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    LocalPrincipalRepository localPrincipalRepository;

    @Mock
    PrincipalFactory passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    AuthManagementService authManagementService;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    ProfileService profileService;

    @DisplayName("Should successfully register local user")
    @Test
    void givenValidLocalRequest_whenRegister_thenSaveUser() {
        // Arrange
        LocalAuthRequest registrationRequest = TestDataGenerator.createLocalAuthRequest();
        User user = User.builder().email(registrationRequest.email()).build();

        when(passwordEncoder.createLocalPrincipal(user, registrationRequest))
                .thenReturn(new LocalPrincipal(user, registrationRequest.email(), "encodedPassword"));

        // Act
        authService.register(registrationRequest);

        // Assert
        verify(passwordEncoder).createLocalPrincipal(user, registrationRequest);
        verify(userRepository).save(user);
    }

    @DisplayName("Should authenticate and return JWT when given valid credentials")
    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnJwtAndUserInfo() {
        // Arrange
        User user = getUser();

        String email = user.getEmail();
        String password = "fdsfsd";
        String expectedJwt = "xxx.yyy.zzz";
        LocalAuthRequest localAuthRequest = new LocalAuthRequest(email, password);
        LocalPrincipal principal =
                LocalPrincipal.builder().user(user).verified(true).build();
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(localPrincipalRepository.findByEmail(email)).thenReturn(principal);
        when(tokenProvider.createToken(any(Authentication.class))).thenReturn(expectedJwt);

        // Act
        JwtAuthResponse result = authService.login(localAuthRequest);

        // Assert
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(profileService).updateLoginStreak(any(Profile.class));
        verify(tokenProvider).createToken(any(Authentication.class));
        assertThat(result.accessToken()).isEqualTo(expectedJwt);
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
}
