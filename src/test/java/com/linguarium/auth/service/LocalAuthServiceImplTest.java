package com.linguarium.auth.service;

import static com.linguarium.user.service.impl.UserUtility.getUser;
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

import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegisterRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.service.impl.LocalLocalAuthServiceImpl;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.user.mapper.UserMapper;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import com.linguarium.user.service.ProfileService;
import com.linguarium.user.service.UserService;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class LocalAuthServiceImplTest {
    @InjectMocks
    LocalLocalAuthServiceImpl authService;

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    UserMapper userMapper;

    @Mock
    ProfileService profileService;

    @DisplayName("Should successfully register local user")
    @Test
    void givenValidLocalRequest_whenRegister_thenSaveUser() {
        // Arrange
        RegisterRequest registrationRequest = RegisterRequest.builder()
                .email("johnwick@gmail.com")
                .username("johnwick")
                .password("password!123")
                .build();

        User user = User.builder()
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .username(registrationRequest.getUsername())
                .build();

        when(userMapper.registerRequestToUser(registrationRequest)).thenReturn(user);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("$2b$encoded/Password");

        // Act
        authService.register(registrationRequest);

        // Assert
        verify(userMapper).registerRequestToUser(registrationRequest);
        verify(passwordEncoder).encode(registrationRequest.getPassword());
        verify(userRepository).save(user);
    }

    @DisplayName("Should authenticate and return JWT when given valid credentials")
    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnJwtAndUserInfo() {
        // Arrange
        User user = getUser();

        String email = user.getEmail();
        String password = user.getPassword();
        String expectedJwt = "xxx.yyy.zzz";
        LoginRequest loginRequest = new LoginRequest(email, password);

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(tokenProvider.createToken(any(Authentication.class))).thenReturn(expectedJwt);

        // Act
        JwtAuthResponse result = authService.login(loginRequest);

        // Assert
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(profileService).updateLoginStreak(any(Profile.class));
        verify(tokenProvider).createToken(any(Authentication.class));
        assertThat(result.accessToken()).isEqualTo(expectedJwt);
    }

    @DisplayName("Should throw an exception when trying to register user with existing email")
    @Test
    void givenExistingUserEmail_whenRegister_thenThrowUserAlreadyExistsAuthenticationException() {
        RegisterRequest registrationRequest =
                RegisterRequest.builder().email("john@mail.com").build();

        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(UserAlreadyExistsAuthenticationException.class);

        verify(userRepository).existsByEmail(registrationRequest.getEmail());
        verify(userRepository, never()).existsById(anyLong());
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).flush();
    }

    @DisplayName("Should throw an exception when trying to register user with existing username")
    @Test
    void givenExistingUsername_whenRegister_thenThrowUserAlreadyExistsAuthenticationException() {
        RegisterRequest registrationRequest =
                RegisterRequest.builder().username("john").build();

        when(userRepository.existsByUsername(registrationRequest.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registrationRequest))
                .isInstanceOf(UserAlreadyExistsAuthenticationException.class);

        verify(userRepository).existsByUsername(registrationRequest.getUsername());
        verify(userRepository, never()).existsById(anyLong());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).flush();
    }
}
