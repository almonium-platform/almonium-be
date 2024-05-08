package com.linguarium.user.service.impl;

import static com.linguarium.user.service.impl.UserUtility.getUser;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.linguarium.auth.dto.AuthProvider;
import com.linguarium.auth.dto.request.LocalRegisterRequest;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.ProviderRegisterRequest;
import com.linguarium.auth.dto.response.JwtAuthResponse;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.config.security.oauth2.userinfo.FacebookOAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.linguarium.user.mapper.UserMapper;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import com.linguarium.user.service.ProfileService;
import com.linguarium.user.service.UserService;
import com.linguarium.util.TestDataGenerator;
import java.util.Collections;
import java.util.Map;
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
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

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
    OAuth2UserInfoFactory userInfoFactory;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    UserMapper userMapper;

    @Mock
    ProfileService profileService;

    @DisplayName("Should throw exception if OAuth2UserInfo doesn't contain a name")
    @Test
    void givenOAuth2UserInfoWithNoName_whenProcessUserRegistration_thenThrowsOAuth2AuthenticationProcessingException() {
        // Arrange
        Map<String, Object> attributes = Collections.singletonMap("email", "johnwick@gmail.com");
        OAuth2UserInfo oAuth2UserInfo = new FacebookOAuth2UserInfo(attributes);
        when(userInfoFactory.getOAuth2UserInfo(any(AuthProvider.class), anyMap()))
                .thenReturn(oAuth2UserInfo);

        // Act & Assert
        assertThatThrownBy(() -> authService.processProviderAuth(
                        AuthProvider.FACEBOOK.name(), attributes, mock(OidcIdToken.class), mock(OidcUserInfo.class)))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class);
    }

    @DisplayName("Should throw exception if OAuth2UserInfo doesn't contain an email")
    @Test
    void givenOAuth2UserInfoWithNoEmail_whenProcessAuthFromProvider_thenThrowsOAuth2ProcessingException() {
        // Arrange
        Map<String, Object> attributes = Collections.singletonMap("name", "John Wick");
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        when(userInfoFactory.getOAuth2UserInfo(any(AuthProvider.class), anyMap()))
                .thenReturn(oAuth2UserInfo);

        // Act & Assert
        assertThatThrownBy(() -> authService.processProviderAuth(
                        AuthProvider.GOOGLE.name(), attributes, mock(OidcIdToken.class), mock(OidcUserInfo.class)))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class);
    }

    @DisplayName("Should throw exception if user is signed up with a different provider")
    @Test
    void givenUserSignedUpWithDifferentProvider_whenProcessUserRegistration_thenThrowsException() {
        AuthServiceImpl userServiceSpy = spy(authService);

        // Arrange
        User user = TestDataGenerator.buildTestUser();
        user.setProfile(Profile.builder().avatarUrl(PROFILE_PIC_LINK).build());
        user.setProvider(AuthProvider.GOOGLE);
        when(userService.findUserByEmail(anyString())).thenReturn(user);
        Map<String, Object> attributes = Map.of("name", "John Wick", "email", "johnwick@gmail.com");
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        when(userInfoFactory.getOAuth2UserInfo(any(AuthProvider.class), anyMap()))
                .thenReturn(oAuth2UserInfo);

        // Act & Assert
        assertThatThrownBy(() -> userServiceSpy.processProviderAuth(
                        AuthProvider.FACEBOOK.name(), attributes, mock(OidcIdToken.class), mock(OidcUserInfo.class)))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class);
    }

    @DisplayName("Should update existing user with new OAuth2UserInfo when user already exists")
    @Test
    void givenExistingUser_whenProcessUserRegistration_thenUpdatesUser() {
        // Arrange
        String registrationId = AuthProvider.GOOGLE.name();
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

        when(userInfoFactory.getOAuth2UserInfo(any(AuthProvider.class), anyMap()))
                .thenReturn(oAuth2UserInfo);
        when(userService.findUserByEmail(email)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        LocalUser result = authService.processProviderAuth(
                registrationId, attributes, mock(OidcIdToken.class), mock(OidcUserInfo.class));

        // Assert
        verify(userRepository).save(existingUser);
        assertThat(existingUser.getProfile().getAvatarUrl()).isEqualTo(newProfilePicLink);
        assertThat(result.getUser()).isEqualTo(existingUser);
    }

    @DisplayName("Should register new user from provider when user is not found")
    @Test
    void givenUserNotFound_whenProcessUserRegistration_thenRegistersNewUser() {
        // Arrange
        String registrationId = "google";
        String email = "johnwick@gmail.com";
        String userId = "101868015518714862283";

        ProviderRegisterRequest registrationRequest = ProviderRegisterRequest.builder()
                .email(email)
                .password(OAUTH2_PLACEHOLDER)
                .provider(AuthProvider.GOOGLE)
                .avatarUrl(PROFILE_PIC_LINK)
                .providerUserId(userId)
                .build();

        Map<String, Object> attributes = createAttributes(email, userId);
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        OidcIdToken idToken = new OidcIdToken("random_token_value", null, null, attributes);

        when(userInfoFactory.getOAuth2UserInfo(eq(AuthProvider.GOOGLE), eq(attributes)))
                .thenReturn(oAuth2UserInfo);
        when(userService.findUserByEmail(eq(email))).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(userMapper.providerRegisterRequestToUser(any(ProviderRegisterRequest.class)))
                .thenReturn(buildFromRegistrationRequest(registrationRequest));

        // Act
        LocalUser result = authService.processProviderAuth(registrationId, attributes, idToken, null);

        // Assert
        verify(userRepository).save(any(User.class));
        assertThat(result.getUser().getEmail()).isEqualTo(email);
        assertThat(result.getIdToken()).isEqualTo(idToken);
        assertThat(result.getAttributes()).isEqualTo(attributes);
    }

    @DisplayName("Should successfully register local user")
    @Test
    void givenValidLocalRequest_whenRegister_thenSaveUser() {
        // Arrange
        LocalRegisterRequest registrationRequest = new LocalRegisterRequest();
        registrationRequest.setEmail("johnwick@gmail.com");
        registrationRequest.setPassword("password!123");
        registrationRequest.setUsername("johnwick");

        User user = User.builder()
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .username(registrationRequest.getUsername())
                .build();

        when(userMapper.localRegisterRequestToUser(registrationRequest)).thenReturn(user);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("$2b$encoded/Password");

        // Act
        authService.register(registrationRequest);

        // Assert
        verify(userMapper).localRegisterRequestToUser(registrationRequest);
        verify(passwordEncoder).encode(registrationRequest.getPassword());
        verify(userRepository).save(user);
    }

    @DisplayName("Should successfully register user from provider")
    @Test
    void givenValidProviderRequestFrom_whenProcessProviderAuth_thenSaveOrUpdateUser() {
        // Arrange
        AuthProvider provider = AuthProvider.GOOGLE;
        Map<String, Object> attributes =
                createAttributes("johnwick@gmail.com", "101868015518714862283", PROFILE_PIC_LINK);
        OidcIdToken idToken = mock(OidcIdToken.class);
        OidcUserInfo userInfo = mock(OidcUserInfo.class);

        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        when(userInfoFactory.getOAuth2UserInfo(eq(provider), anyMap())).thenReturn(oAuth2UserInfo);

        ProviderRegisterRequest providerRequest = ProviderRegisterRequest.builder()
                .email("johnwick@gmail.com")
                .providerUserId("101868015518714862283")
                .avatarUrl(PROFILE_PIC_LINK)
                .provider(AuthProvider.GOOGLE)
                .build();

        User newUser = User.builder()
                .email(providerRequest.getEmail())
                .providerUserId(providerRequest.getProviderUserId())
                .profile(Profile.builder()
                        .avatarUrl(providerRequest.getAvatarUrl())
                        .build())
                .build();

        when(userService.findUserByEmail("johnwick@gmail.com")).thenReturn(null);
        when(userMapper.providerRegisterRequestToUser(any(ProviderRegisterRequest.class)))
                .thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Act
        LocalUser result = authService.processProviderAuth(provider.name(), attributes, idToken, userInfo);

        // Assert
        verify(userInfoFactory).getOAuth2UserInfo(provider, attributes);
        verify(userService).findUserByEmail("johnwick@gmail.com");
        verify(userMapper).providerRegisterRequestToUser(any(ProviderRegisterRequest.class));
        verify(userRepository).save(newUser);
        assertThat(result.getUser()).isEqualTo(newUser);
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

        LocalUser localUser = new LocalUser(user, null, null, null);
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(localUser);
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
        LocalRegisterRequest registrationRequest = new LocalRegisterRequest();
        registrationRequest.setEmail("john@example.com");

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
        LocalRegisterRequest registrationRequest = new LocalRegisterRequest();
        registrationRequest.setUsername("john");

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

    private User buildFromRegistrationRequest(ProviderRegisterRequest request) {
        return User.builder()
                .providerUserId(request.getProviderUserId())
                .provider(request.getProvider())
                .password(request.getPassword())
                .email(request.getEmail())
                .username(request.getUsername())
                .build();
    }
}
