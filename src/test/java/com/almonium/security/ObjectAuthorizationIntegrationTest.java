package com.almonium.security;

import static com.almonium.auth.firebase.service.FirebaseSessionCookieService.COOKIE_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.almonium.auth.firebase.filter.FirebaseSessionAuthenticationFilter;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.auth.firebase.service.FirebaseSessionCookieService;
import com.almonium.auth.firebase.service.FirebaseSessionService;
import com.almonium.auth.firebase.service.FirebaseUserProvisioningService;
import com.almonium.config.WebMvcConfig;
import com.almonium.config.security.WebSecurityConfig;
import com.almonium.infra.notification.controller.NotificationController;
import com.almonium.infra.notification.mapper.NotificationMapper;
import com.almonium.infra.notification.repository.NotificationRepository;
import com.almonium.infra.notification.service.FCMService;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.infra.storage.service.FirebaseStorageService;
import com.almonium.user.core.controller.AvatarController;
import com.almonium.user.core.mapper.AvatarMapper;
import com.almonium.user.core.model.entity.Avatar;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.AvatarRepository;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.AvatarService;
import com.almonium.user.core.service.ProfileInfoService;
import com.almonium.user.core.service.ProfileService;
import com.almonium.user.core.service.RelationshipActionsFacade;
import com.almonium.user.relationship.controller.RelationshipController;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelationshipStatus;
import com.almonium.user.relationship.repository.RelationshipRepository;
import com.almonium.user.relationship.service.RelationshipPerspectiveResolver;
import com.almonium.user.relationship.service.RelationshipService;
import com.almonium.user.relationship.service.RelationshipStateMachine;
import com.almonium.util.config.TestConfig;
import jakarta.servlet.http.Cookie;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {AvatarController.class, NotificationController.class, RelationshipController.class})
@Import({
    WebSecurityConfig.class,
    WebMvcConfig.class,
    FirebaseSessionAuthenticationFilter.class,
    FirebaseSessionCookieService.class,
    FirebaseSessionService.class,
    AvatarService.class,
    NotificationService.class,
    RelationshipService.class,
    RelationshipActionsFacade.class,
    RelationshipStateMachine.class,
    RelationshipPerspectiveResolver.class,
    TestConfig.class
})
class ObjectAuthorizationIntegrationTest {
    private static final String UID = "current-firebase-uid";
    private static final String SESSION = "current-user-session";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FirebaseAuthGateway firebaseAuthGateway;

    @MockitoBean
    FirebaseUserProvisioningService firebaseUserProvisioningService;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    ProfileService profileService;

    @MockitoBean
    ProfileInfoService profileInfoService;

    @MockitoBean
    AvatarRepository avatarRepository;

    @MockitoBean
    ProfileRepository profileRepository;

    @MockitoBean
    AvatarMapper avatarMapper;

    @MockitoBean
    FirebaseStorageService firebaseStorageService;

    @MockitoBean
    NotificationRepository notificationRepository;

    @MockitoBean
    NotificationMapper notificationMapper;

    @MockitoBean
    FCMService fcmService;

    @MockitoBean
    RelationshipRepository relationshipRepository;

    private User currentUser;

    @BeforeEach
    void authenticateCurrentUser() {
        currentUser = User.builder()
                .id(UUID.randomUUID())
                .firebaseUid(UID)
                .email("current@example.com")
                .build();
        FirebaseIdentity identity = new FirebaseIdentity(UID, currentUser.getEmail(), true, Instant.now(), "password");
        when(firebaseAuthGateway.verifySessionCookie(SESSION, false)).thenReturn(identity);
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(currentUser));
    }

    @Test
    void currentUserCannotChooseAnotherUsersAvatar() throws Exception {
        UUID avatarId = UUID.randomUUID();
        Avatar foreignAvatar = foreignAvatar(avatarId);
        when(avatarRepository.findById(avatarId)).thenReturn(Optional.of(foreignAvatar));

        mockMvc.perform(patch("/profiles/me/avatars/{avatarId}", avatarId)
                        .cookie(sessionCookie())
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));

        verify(profileService, never()).getProfileById(currentUser.getId());
        verify(profileRepository, never()).save(any());
    }

    @Test
    void currentUserCannotDeleteAnotherUsersAvatar() throws Exception {
        UUID avatarId = UUID.randomUUID();
        Avatar foreignAvatar = foreignAvatar(avatarId);
        when(avatarRepository.findById(avatarId)).thenReturn(Optional.of(foreignAvatar));

        mockMvc.perform(delete("/profiles/me/avatars/{avatarId}", avatarId)
                        .cookie(sessionCookie())
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(firebaseStorageService, never()).deleteFile(any());
        verify(avatarRepository, never()).delete(any());
    }

    @Test
    void notificationDeletionIsAlwaysScopedToAuthenticatedRecipient() throws Exception {
        UUID foreignNotificationId = UUID.randomUUID();

        mockMvc.perform(delete("/notifications/{id}", foreignNotificationId)
                        .cookie(sessionCookie())
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(notificationRepository).deleteByIdAndRecipient(foreignNotificationId, currentUser);
        verify(notificationRepository, never()).deleteById(foreignNotificationId);
    }

    @Test
    void notificationStateChangesAreAlwaysScopedToAuthenticatedRecipient() throws Exception {
        UUID foreignNotificationId = UUID.randomUUID();

        mockMvc.perform(patch("/notifications/{id}/read", foreignNotificationId)
                        .cookie(sessionCookie())
                        .with(csrf()))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/notifications/{id}/unread", foreignNotificationId)
                        .cookie(sessionCookie())
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(notificationRepository).readNotification(currentUser, foreignNotificationId);
        verify(notificationRepository).unreadNotification(currentUser, foreignNotificationId);
    }

    @Test
    void unrelatedUserCannotMutateAnotherUsersRelationship() throws Exception {
        UUID relationshipId = UUID.randomUUID();
        Relationship foreignRelationship = Relationship.builder()
                .id(relationshipId)
                .requester(User.builder().id(UUID.randomUUID()).build())
                .requestee(User.builder().id(UUID.randomUUID()).build())
                .status(RelationshipStatus.PENDING)
                .build();
        when(relationshipRepository.findById(relationshipId)).thenReturn(Optional.of(foreignRelationship));

        mockMvc.perform(patch("/relationships/{id}", relationshipId)
                        .cookie(sessionCookie())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"action\":\"ACCEPT\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User is not part of this relationship"));

        verify(relationshipRepository, never()).save(any());
    }

    private Avatar foreignAvatar(UUID avatarId) {
        User foreignUser = User.builder().id(UUID.randomUUID()).build();
        Profile foreignProfile =
                Profile.builder().id(foreignUser.getId()).user(foreignUser).build();
        return Avatar.builder()
                .id(avatarId)
                .profile(foreignProfile)
                .url("https://example.test/avatar.png")
                .build();
    }

    private Cookie sessionCookie() {
        return new Cookie(COOKIE_NAME, SESSION);
    }
}
