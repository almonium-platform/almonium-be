package com.linguarium.user.service.impl;

import static com.linguarium.user.service.impl.UserUtility.getUser;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.linguarium.auth.dto.AuthProvider;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegisterRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.config.security.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.user.mapper.UserMapper;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import com.linguarium.user.service.ProfileService;
import com.linguarium.user.service.UserService;
import com.linguarium.util.TestDataGenerator;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AuthServiceImplTest {
    private static final String PROFILE_PIC_LINK =
            "https://lh3.googleusercontent.com/a/AAcHTtdmMGFI1asVb1fp_pQ1ypkJqEHmI6Ug67ntQfLHYNqapw=s94-c";
    private static final String OAUTH2_PLACEHOLDER = "OAUTH2_PLACEHOLDER";

    @InjectMocks
    AuthServiceImpl authService;

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

    @DisplayName("Should throw exception if user is signed up with a different provider")
    @Test
    void givenUserSignedUpWithDifferentProvider_whenProcessUserRegistration_thenThrowsException() {
        AuthServiceImpl userServiceSpy = spy(authService);

        // Arrange
        User user = TestDataGenerator.buildTestUser();
        user.setProfile(Profile.builder().avatarUrl(PROFILE_PIC_LINK).build());
        user.setProvider(AuthProvider.FACEBOOK);
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        Map<String, Object> attributes = Map.of("name", "John Wick", "email", "johnwick@gmail.com");
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        // Act & Assert
        assertThatThrownBy(() -> userServiceSpy.authenticateProviderRequest(oAuth2UserInfo))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class);
    }

    @DisplayName("Should update existing user with new OAuth2UserInfo when user already exists")
    @Test
    void givenExistingUser_whenProcessUserRegistration_thenUpdatesUser() {
        // Arrange
        String email = "johnwick@gmail.com";
        String newProfilePicLink = "https://new-image-link.com";
        Map<String, Object> attributes = createAttributes(email, "101868015518714862283", newProfilePicLink);
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        User existingUser = User.builder()
                .email(email)
                .password(OAUTH2_PLACEHOLDER)
                .provider(AuthProvider.GOOGLE)
                .profile(Profile.builder()
                        .avatarUrl("https://old-image-link.com")
                        .build())
                .build();

        when(userService.findUserByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        User result = authService.authenticateProviderRequest(oAuth2UserInfo);

        // Assert
        verify(userRepository).save(existingUser);
        assertThat(existingUser.getProfile().getAvatarUrl()).isEqualTo(newProfilePicLink);
        assertThat(result).isEqualTo(existingUser);
    }

    @DisplayName("Should register new user from provider when user is not found")
    @Test
    void givenUserNotFound_whenProcessUserRegistration_thenRegistersNewUser() {
        // Arrange
        AuthProvider provider = AuthProvider.GOOGLE;
        String email = "johnwick@gmail.com";
        String userId = "101868015518714862283";

        Map<String, Object> attributes = createAttributes(email, userId);
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        when(userService.findUserByEmail(eq(email))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setProfile(Profile.builder().user(user).build());
            return user;
        });
        when(userMapper.providerUserInfoToUser(eq(oAuth2UserInfo)))
                .thenReturn(User.builder()
                        .email(email)
                        .providerUserId(userId)
                        .provider(provider)
                        .build());

        // Act
        User result = authService.authenticateProviderRequest(oAuth2UserInfo);

        // Assert
        verify(userRepository, atLeastOnce()).save(any(User.class));
        assertThat(result.getEmail()).isEqualTo(email);
    }

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

    @DisplayName("Should successfully register user from provider")
    @Test
    void givenValidProviderRequestFrom_whenAuthenticateProviderRequest_thenSaveOrUpdateUser() {
        OAuth2UserInfo oAuth2UserInfo =
                new GoogleOAuth2UserInfo(createAttributes("johnwick@gmail.com", "101868015518714862283"));

        User newUser = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .providerUserId(oAuth2UserInfo.getId())
                .profile(Profile.builder()
                        .avatarUrl(oAuth2UserInfo.getImageUrl())
                        .build())
                .build();

        when(userService.findUserByEmail("johnwick@gmail.com")).thenReturn(Optional.empty());
        when(userMapper.providerUserInfoToUser(eq(oAuth2UserInfo))).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Act
        User result = authService.authenticateProviderRequest(oAuth2UserInfo);

        // Assert
        verify(userService).findUserByEmail("johnwick@gmail.com");
        verify(userMapper).providerUserInfoToUser(eq(oAuth2UserInfo));
        verify(userRepository, atLeastOnce()).save(newUser);
        assertThat(result).isEqualTo(newUser);
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

    private Map<String, Object> createAttributes(String email, String userId) {
        return createAttributes(email, userId, PROFILE_PIC_LINK);
    }

    private Map<String, Object> createAttributes(String email, String userId, String profilePicUrl) {
        return Map.ofEntries(
                entry("at_hash", "RkJFPU-iZS_amRETFhGrdA"),
                entry("sub", userId),
                entry("email_verified", true),
                entry("iss", "https://accounts.google.com"),
                entry("given_name", "John"),
                entry("locale", "uk"),
                entry("nonce", "K3TiqNu1cgnErWX962crIutE8YiEjuQAd3PDzUV0E5M"),
                entry("picture", profilePicUrl),
                entry("aud", new String[] {"832714080763-hj64thg1sghaubbg9m6qd288mbv09li6.apps.googleusercontent.com"}),
                entry("azp", "832714080763-hj64thg1sghaubbg9m6qd288mbv09li6.apps.googleusercontent.com"),
                entry("name", "John Wick"),
                entry("exp", "2023-06-27T15:00:44Z"),
                entry("family_name", "Wick"),
                entry("iat", "2023-06-27T14:00:44Z"),
                entry("email", email));
    }
}
