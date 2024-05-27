package linguarium.auth.oauth2.service;

import static java.util.Map.entry;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import linguarium.auth.oauth2.exception.OAuth2AuthenticationProcessingException;
import linguarium.auth.oauth2.model.AuthProviderType;
import linguarium.auth.oauth2.model.userinfo.GoogleOAuth2UserInfo;
import linguarium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import linguarium.user.core.mapper.UserMapper;
import linguarium.user.core.model.Profile;
import linguarium.user.core.model.User;
import linguarium.user.core.repository.UserRepository;
import linguarium.util.TestDataGenerator;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class ProviderLocalAuthServiceImplTest {
    private static final String PROFILE_PIC_LINK =
            "https://lh3.googleusercontent.com/a/AAcHTtdmMGFI1asVb1fp_pQ1ypkJqEHmI6Ug67ntQfLHYNqapw=s94-c";
    private static final String OAUTH2_PLACEHOLDER = "OAUTH2_PLACEHOLDER";

    @InjectMocks
    ProviderAuthServiceImpl authService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @DisplayName("Should throw exception if user is signed up with a different provider")
    @Test
    void givenUserSignedUpWithDifferentProvider_whenProcessUserRegistration_thenThrowsException() {
        ProviderAuthServiceImpl userServiceSpy = spy(authService);

        // Arrange
        User user = TestDataGenerator.buildTestUser();
        user.setProfile(Profile.builder().avatarUrl(PROFILE_PIC_LINK).build());
        user.setProvider(AuthProviderType.FACEBOOK);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        Map<String, Object> attributes = Map.of("name", "John Wick", "email", "johnwick@gmail.com");
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        // Act & Assert
        assertThatThrownBy(() -> userServiceSpy.authenticate(oAuth2UserInfo))
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
                .provider(AuthProviderType.GOOGLE)
                .profile(Profile.builder()
                        .avatarUrl("https://old-image-link.com")
                        .build())
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        User result = authService.authenticate(oAuth2UserInfo);

        // Assert
        verify(userRepository).save(existingUser);
        assertThat(existingUser.getProfile().getAvatarUrl()).isEqualTo(newProfilePicLink);
        assertThat(result).isEqualTo(existingUser);
    }

    @DisplayName("Should register new user from provider when user is not found")
    @Test
    void givenUserNotFound_whenProcessUserRegistration_thenRegistersNewUser() {
        // Arrange
        AuthProviderType provider = AuthProviderType.GOOGLE;
        String email = "johnwick@gmail.com";
        String userId = "101868015518714862283";

        Map<String, Object> attributes = createAttributes(email, userId);
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.empty());
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
        User result = authService.authenticate(oAuth2UserInfo);

        // Assert
        verify(userRepository, atLeastOnce()).save(any(User.class));
        assertThat(result.getEmail()).isEqualTo(email);
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

        when(userRepository.findByEmail("johnwick@gmail.com")).thenReturn(Optional.empty());
        when(userMapper.providerUserInfoToUser(eq(oAuth2UserInfo))).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Act
        User result = authService.authenticate(oAuth2UserInfo);

        // Assert
        verify(userRepository).findByEmail("johnwick@gmail.com");
        verify(userMapper).providerUserInfoToUser(eq(oAuth2UserInfo));
        verify(userRepository, atLeastOnce()).save(newUser);
        assertThat(result).isEqualTo(newUser);
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
                entry("aud", new String[]{"832714080763-hj64thg1sghaubbg9m6qd288mbv09li6.apps.googleusercontent.com"}),
                entry("azp", "832714080763-hj64thg1sghaubbg9m6qd288mbv09li6.apps.googleusercontent.com"),
                entry("name", "John Wick"),
                entry("exp", "2023-06-27T15:00:44Z"),
                entry("family_name", "Wick"),
                entry("iat", "2023-06-27T14:00:44Z"),
                entry("email", email));
    }
}
