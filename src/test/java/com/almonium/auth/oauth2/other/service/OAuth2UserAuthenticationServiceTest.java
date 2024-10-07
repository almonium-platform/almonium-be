package com.almonium.auth.oauth2.other.service;

import static java.util.Map.entry;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.local.exception.EmailMismatchException;
import com.almonium.auth.oauth2.other.model.entity.OAuth2Principal;
import com.almonium.auth.oauth2.other.model.enums.OAuth2Intent;
import com.almonium.auth.oauth2.other.model.userinfo.GoogleOAuth2UserInfo;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo;
import com.almonium.auth.oauth2.other.repository.OAuth2PrincipalRepository;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UserService;
import java.util.Map;
import java.util.Optional;
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
class OAuth2UserAuthenticationServiceTest {
    private static final String PROFILE_PIC_LINK =
            "https://lh3.googleusercontent.com/a/AAcHTtdmMGFI1asVb1fp_pQ1ypkJqEHmI6Ug67ntQfLHYNqapw=s94-c";

    @InjectMocks
    OAuth2AuthenticationService authService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    OAuth2PrincipalRepository oAuth2PrincipalRepository;

    @DisplayName("Should create new principal if user exists but principal is not found")
    @Test
    void givenExistingUserWithoutPrincipal_whenAuthenticate_thenCreateNewPrincipal() {
        // Arrange
        String email = "johnwick@gmail.com";
        String newProfilePicLink = "https://old-image-link.com";
        String userId = "101868015518714862283";
        Map<String, Object> attributes = createAttributes(email, userId, newProfilePicLink);
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        User existingUser = User.builder()
                .email(email)
                .username("google-101868015518714862283")
                .profile(Profile.builder().avatarUrl(newProfilePicLink).build())
                .build();

        OAuth2Principal newPrincipal = OAuth2Principal.builder()
                .email(email)
                .providerUserId(userId)
                .provider(oAuth2UserInfo.getProvider())
                .user(existingUser)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(oAuth2PrincipalRepository.findByProviderAndProviderUserId(AuthProviderType.GOOGLE, userId))
                .thenReturn(Optional.empty());
        when(userMapper.providerUserInfoToPrincipal(eq(oAuth2UserInfo))).thenReturn(newPrincipal);
        when(oAuth2PrincipalRepository.save(any(OAuth2Principal.class))).thenReturn(newPrincipal);
        when(userRepository.save(existingUser)).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        Principal result = authService.authenticate(oAuth2UserInfo, OAuth2Intent.SIGN_IN);

        // Assert
        verify(userRepository).save(existingUser);
        verify(oAuth2PrincipalRepository).save(newPrincipal);
        assertThat(result).isEqualTo(newPrincipal);
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
                .username("google-101868015518714862283")
                .profile(Profile.builder()
                        .avatarUrl("https://old-image-link.com")
                        .build())
                .build();
        OAuth2Principal principal = OAuth2Principal.builder()
                .provider(AuthProviderType.GOOGLE)
                .providerUserId("101868015518714862283")
                .build();
        existingUser.getPrincipals().add(principal);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(oAuth2PrincipalRepository.findByProviderAndProviderUserId(
                        AuthProviderType.GOOGLE, "101868015518714862283"))
                .thenReturn(Optional.of(principal));
        when(userRepository.save(any(User.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        when(oAuth2PrincipalRepository.save(any(OAuth2Principal.class))).thenReturn(principal);

        // Act
        Principal result = authService.authenticate(oAuth2UserInfo, OAuth2Intent.SIGN_IN);

        // Assert
        verify(userRepository).save(existingUser);
        verify(oAuth2PrincipalRepository).save(any());
        assertThat(existingUser.getProfile().getAvatarUrl()).isEqualTo(newProfilePicLink);
        assertThat(result).isEqualTo(principal);
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
        when(oAuth2PrincipalRepository.save(any(OAuth2Principal.class))).thenAnswer(invocation -> {
            Principal auth = invocation.getArgument(0);
            auth.setId(1L);
            return auth;
        });
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setProfile(Profile.builder().user(user).build());
            return user;
        });
        when(userMapper.providerUserInfoToPrincipal(eq(oAuth2UserInfo)))
                .thenReturn(OAuth2Principal.builder()
                        .email(email)
                        .providerUserId(userId)
                        .provider(provider)
                        .build());

        // Act
        Principal result = authService.authenticate(oAuth2UserInfo, OAuth2Intent.SIGN_IN);

        // Assert
        verify(userRepository, atLeastOnce()).save(any(User.class));
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @DisplayName("Should successfully register user from provider")
    @Test
    void givenValidProviderRequestFrom_whenAuthenticateProviderRequest_thenSaveOrUpdateUser() {
        // Arrange
        OAuth2UserInfo oAuth2UserInfo =
                new GoogleOAuth2UserInfo(createAttributes("johnwick@gmail.com", "101868015518714862283"));

        User newUser = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .username(String.format("%s-%s", oAuth2UserInfo.getProvider(), oAuth2UserInfo.getId()))
                .profile(Profile.builder()
                        .avatarUrl(oAuth2UserInfo.getImageUrl())
                        .build())
                .build();
        OAuth2Principal principal = OAuth2Principal.builder()
                .email(oAuth2UserInfo.getEmail())
                .providerUserId(oAuth2UserInfo.getId())
                .provider(oAuth2UserInfo.getProvider())
                .user(newUser)
                .build();

        when(userRepository.findByEmail("johnwick@gmail.com")).thenReturn(Optional.empty());
        when(userMapper.providerUserInfoToPrincipal(eq(oAuth2UserInfo))).thenReturn(principal);
        when(userRepository.save(newUser)).thenReturn(newUser);
        when(oAuth2PrincipalRepository.save(principal)).thenReturn(principal);

        // Act
        Principal result = authService.authenticate(oAuth2UserInfo, OAuth2Intent.SIGN_IN);

        // Assert
        verify(userRepository).findByEmail("johnwick@gmail.com");
        verify(userMapper).providerUserInfoToPrincipal(eq(oAuth2UserInfo));
        verify(userRepository, atLeastOnce()).save(newUser);
        verify(oAuth2PrincipalRepository).save(principal);
        assertThat(result).isEqualTo(principal);
    }

    @DisplayName("Should throw EmailMismatchException when linking account and user is not found")
    @Test
    void givenOAuth2IntentLinkAndUserNotFound_whenAuthenticate_thenThrowEmailMismatchException() {
        // Arrange
        String email = "johnwick@gmail.com";
        String userId = "101868015518714862283";
        Map<String, Object> attributes = createAttributes(email, userId);
        OAuth2UserInfo oAuth2UserInfo = new GoogleOAuth2UserInfo(attributes);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> authService.authenticate(oAuth2UserInfo, OAuth2Intent.LINK))
                .isInstanceOf(EmailMismatchException.class)
                .hasMessageContaining("No user found for email " + email + " to link account.");

        verify(userRepository).findByEmail(email);
        verify(oAuth2PrincipalRepository, never()).save(any(OAuth2Principal.class));
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
