package com.linguarium.user.service.impl;

import static com.linguarium.user.service.impl.UserUtility.getUser;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.within;
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

import com.linguarium.auth.dto.SocialProvider;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.RegistrationRequest;
import com.linguarium.auth.dto.response.JwtAuthenticationResponse;
import com.linguarium.auth.exception.OAuth2AuthenticationProcessingException;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.repository.CardTagRepository;
import com.linguarium.card.repository.TagRepository;
import com.linguarium.config.security.jwt.TokenProvider;
import com.linguarium.config.security.oauth2.userinfo.FacebookOAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.linguarium.translator.model.Language;
import com.linguarium.user.mapper.UserMapper;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import com.linguarium.user.service.ProfileService;
import com.linguarium.util.TestDataGenerator;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
class UserServiceImplTest {
    private static final String PROFILE_PIC_LINK =
            "https://lh3.googleusercontent.com/a/AAcHTtdmMGFI1asVb1fp_pQ1ypkJqEHmI6Ug67ntQfLHYNqapw=s94-c";
    private static final String OAUTH2_PLACEHOLDER = "OAUTH2_PLACEHOLDER";

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    CardTagRepository cardTagRepository;

    @Mock
    TagRepository tagRepository;

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
        when(userInfoFactory.getOAuth2UserInfo(anyString(), anyMap())).thenReturn(oAuth2UserInfo);

        // Act & Assert
        assertThatThrownBy(() -> userService.processProviderAuth(
                        SocialProvider.FACEBOOK.getProviderType(),
                        attributes,
                        mock(OidcIdToken.class),
                        mock(OidcUserInfo.class)))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class);
    }

    @DisplayName("Should throw exception if OAuth2UserInfo doesn't contain an email")
    @Test
    void givenOAuth2UserInfoWithNoEmail_whenProcessAuthFromProvider_thenThrowsOAuth2ProcessingException() {
        // Arrange
        Map<String, Object> attributes = Collections.singletonMap("name", "John Wick");
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        when(userInfoFactory.getOAuth2UserInfo(anyString(), anyMap())).thenReturn(oAuth2UserInfo);

        // Act & Assert
        assertThatThrownBy(() -> userService.processProviderAuth(
                        SocialProvider.GOOGLE.getProviderType(),
                        attributes,
                        mock(OidcIdToken.class),
                        mock(OidcUserInfo.class)))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class);
    }

    @DisplayName("Should throw exception if user is signed up with a different provider")
    @Test
    void givenUserSignedUpWithDifferentProvider_whenProcessUserRegistration_thenThrowsException() {
        UserServiceImpl userServiceSpy = spy(userService);

        // Arrange
        String existingProvider = SocialProvider.GOOGLE.getProviderType();
        User user = TestDataGenerator.buildTestUser();
        user.setProfile(Profile.builder().profilePicLink(PROFILE_PIC_LINK).build());
        user.setProvider(existingProvider);
        when(userServiceSpy.findUserByEmail(anyString())).thenReturn(user);
        Map<String, Object> attributes = Map.of("name", "John Wick", "email", "johnwick@gmail.com");
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        when(userInfoFactory.getOAuth2UserInfo(anyString(), anyMap())).thenReturn(oAuth2UserInfo);

        // Act & Assert
        assertThatThrownBy(() -> userServiceSpy.processProviderAuth(
                        SocialProvider.FACEBOOK.getProviderType(),
                        attributes,
                        mock(OidcIdToken.class),
                        mock(OidcUserInfo.class)))
                .isInstanceOf(OAuth2AuthenticationProcessingException.class);
    }

    @DisplayName("Should update existing user with new OAuth2UserInfo when user already exists")
    @Test
    void givenExistingUser_whenProcessUserRegistration_thenUpdatesUser() {
        // Arrange
        String registrationId = SocialProvider.GOOGLE.getProviderType();
        String email = "johnwick@gmail.com";
        String newProfilePicLink = "https://new-image-link.com";
        Map<String, Object> attributes = createAttributes(email, "101868015518714862283", newProfilePicLink);
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        User existingUser = User.builder()
                .email(email)
                .password(OAUTH2_PLACEHOLDER)
                .provider(SocialProvider.GOOGLE.getProviderType())
                .profile(Profile.builder()
                        .profilePicLink("https://old-image-link.com")
                        .build())
                .build();

        when(userInfoFactory.getOAuth2UserInfo(anyString(), anyMap())).thenReturn(oAuth2UserInfo);
        when(userRepository.findByEmail(email)).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        LocalUser result = userService.processProviderAuth(
                registrationId, attributes, mock(OidcIdToken.class), mock(OidcUserInfo.class));

        // Assert
        verify(userRepository).save(existingUser);
        assertThat(existingUser.getProfile().getProfilePicLink()).isEqualTo(newProfilePicLink);
        assertThat(result.getUser()).isEqualTo(existingUser);
    }

    @DisplayName("Should register new user from provider when user is not found")
    @Test
    void givenUserNotFound_whenProcessUserRegistration_thenRegistersNewUser() {
        // Arrange
        String registrationId = "google";
        String email = "johnwick@gmail.com";
        String userId = "101868015518714862283";

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .email(email)
                .password(OAUTH2_PLACEHOLDER)
                .socialProvider(SocialProvider.GOOGLE)
                .profilePicLink(PROFILE_PIC_LINK)
                .providerUserId(userId)
                .build();

        Map<String, Object> attributes = createAttributes(email, userId);
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);
        OidcIdToken idToken = new OidcIdToken("random_token_value", null, null, attributes);

        when(userInfoFactory.getOAuth2UserInfo(anyString(), anyMap())).thenReturn(oAuth2UserInfo);
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(userMapper.registrationRequestToUser(registrationRequest))
                .thenReturn(buildFromRegistrationRequest(registrationRequest));

        // Act
        LocalUser result = userService.processProviderAuth(registrationId, attributes, idToken, null);

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
        SocialProvider provider = SocialProvider.LOCAL;
        String email = "johnwick@gmail.com";
        String password = "password!123";
        String encodedPassword = "$2a$12$ugbD8fSrfyP3BoaM/nyK1OUheCxtTwtANKfUG0VnYq5BWQSurW2g2";

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .email(email)
                .password(password)
                .socialProvider(provider)
                .profilePicLink(PROFILE_PIC_LINK)
                .build();

        User expectedUser = User.builder()
                .email(email)
                .password(encodedPassword)
                .provider(provider.getProviderType())
                .build();

        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(passwordEncoder.encode(eq(password))).thenReturn(encodedPassword);
        when(userMapper.registrationRequestToUser(registrationRequest))
                .thenReturn(buildFromRegistrationRequest(registrationRequest));

        // Act
        User actualUser = userService.register(registrationRequest);

        // Assert
        assertThat(actualUser)
                .usingRecursiveComparison()
                .ignoringFields("registered", "username", "profile", "learner")
                .isEqualTo(expectedUser);

        assertThat(actualUser.getRegistered()).isEqualTo(actualUser.getProfile().getLastLogin());

        assertThat(actualUser.getRegistered()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));

        assertThat(actualUser.getProfile()).isNotNull();
        assertThat(actualUser.getLearner()).isNotNull();
        assertThat(actualUser.getProfile().getProfilePicLink()).isEqualTo(PROFILE_PIC_LINK);
        assertThat(actualUser.getUsername()).isNotBlank();

        verify(userRepository).save(any(User.class));
    }

    @DisplayName("Should successfully register user from provider")
    @Test
    void givenValidProviderRequestFrom_whenRegister_thenSaveUser() {
        // Arrange
        SocialProvider provider = SocialProvider.GOOGLE;
        String userId = "101868015518714862283";
        String email = "johnwick@gmail.com";

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .email(email)
                .password(OAUTH2_PLACEHOLDER)
                .providerUserId(userId)
                .socialProvider(provider)
                .profilePicLink(PROFILE_PIC_LINK)
                .build();

        User expectedUser = User.builder()
                .email(email)
                .password(OAUTH2_PLACEHOLDER)
                .provider(provider.getProviderType())
                .providerUserId(userId)
                .build();

        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(userMapper.registrationRequestToUser(registrationRequest))
                .thenReturn(buildFromRegistrationRequest(registrationRequest));

        // Act
        User actualUser = userService.register(registrationRequest);

        // Assert
        assertThat(actualUser)
                .usingRecursiveComparison()
                .ignoringFields("registered", "username", "profile", "learner")
                .isEqualTo(expectedUser);

        assertThat(actualUser.getRegistered()).isEqualTo(actualUser.getProfile().getLastLogin());

        assertThat(actualUser.getRegistered()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));

        assertThat(actualUser.getProfile()).isNotNull();
        assertThat(actualUser.getLearner()).isNotNull();
        assertThat(actualUser.getProfile().getProfilePicLink()).isEqualTo(PROFILE_PIC_LINK);
        assertThat(actualUser.getUsername()).isNotBlank();

        verify(userRepository).save(any(User.class));
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
        JwtAuthenticationResponse result = userService.login(loginRequest);

        // Assert
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(profileService).updateLoginStreak(any(Profile.class));
        verify(tokenProvider).createToken(any(Authentication.class));
        assertThat(result.accessToken()).isEqualTo(expectedJwt);
    }

    @DisplayName("Should throw an exception when trying to register user with existing email")
    @Test
    void givenExistingUserEmail_whenRegister_thenThrowUserAlreadyExistsAuthenticationException() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("john@example.com");

        when(userRepository.existsByEmail(registrationRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(registrationRequest))
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
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("john");

        when(userRepository.existsByUsername(registrationRequest.getUsername())).thenReturn(true);

        assertThatThrownBy(() -> userService.register(registrationRequest))
                .isInstanceOf(UserAlreadyExistsAuthenticationException.class);

        verify(userRepository).existsByUsername(registrationRequest.getUsername());
        verify(userRepository, never()).existsById(anyLong());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, never()).flush();
    }

    @DisplayName("Should change username")
    @Test
    void givenUsername_whenChangeUsername_thenUsernameByIdChanged() {
        String username = "newUsername";
        Long id = 1L;

        userService.changeUsernameById(username, id);

        verify(userRepository).changeUsername(username, id);
    }

    @DisplayName("Should return user optional for existing user")
    @Test
    void givenExistingUser_whenFindUserById_thenReturnUserOptional() {
        Long userId = 1L;
        User user = getUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(userId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
        verify(userRepository).findById(userId);
    }

    @DisplayName("Should return empty optional for non existing user")
    @Test
    void givenNonExistingUser_whenFindUserById_thenReturnEmptyOptional() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(userId);

        assertThat(result).isEmpty();
        verify(userRepository).findById(userId);
    }

    @DisplayName("Should delete user account")
    @Test
    void givenUser_whenDeleteAccount_thenRepositoryDeleteIsCalled() {
        User user = getUser();

        userService.deleteAccount(user);

        verify(userRepository).delete(user);
    }

    @DisplayName("Should return user if email exists")
    @Test
    void givenExistentEmail_whenFindByEmail_thenReturnUser() {
        String email = "john@example.com";
        User expectedUser = getUser();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        User actualUser = userService.findUserByEmail(email);

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @DisplayName("Should return null if email doesn't exist")
    @Test
    void givenNonExistentEmail_whenFindByEmail_thenReturnNull() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        User actualUser = userService.findUserByEmail(email);

        assertThat(actualUser).isNull();
    }

    @DisplayName("Should use mapper to build userInfo")
    @Test
    void givenLocalUser_whenBuildUserInfo_thenInvokeMapper() {
        User user = getUser();
        userService.buildUserInfoFromUser(user);
        verify(userMapper).userToUserInfo(user);
    }

    @DisplayName("Should return true when username is available")
    @Test
    void givenAvailableUsername_whenIsUsernameAvailable_thenReturnsTrue() {
        // Arrange
        String username = "newUsername";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = userService.isUsernameAvailable(username);

        // Assert
        assertThat(result).isTrue();
    }

    @DisplayName("Should return false when username is already taken")
    @Test
    void givenTakenUsername_whenIsUsernameAvailable_thenReturnsFalse() {
        // Arrange
        String username = "existingUsername";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = userService.isUsernameAvailable(username);

        // Assert
        assertThat(result).isFalse();
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

    private User buildFromRegistrationRequest(RegistrationRequest request) {
        return User.builder()
                .providerUserId(request.getProviderUserId())
                .provider(request.getSocialProvider().getProviderType())
                .password(request.getPassword())
                .email(request.getEmail())
                .username(request.getUsername())
                .build();
    }
}
