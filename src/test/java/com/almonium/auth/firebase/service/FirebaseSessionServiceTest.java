package com.almonium.auth.firebase.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FirebaseSessionServiceTest {
    @Mock
    FirebaseAuthGateway gateway;

    @Mock
    FirebaseUserProvisioningService provisioningService;

    @Mock
    FirebaseSessionCookieService cookieService;

    @Mock
    ProfileService profileService;

    @Mock
    HttpServletResponse response;

    FirebaseSessionService service;

    @BeforeEach
    void setUp() {
        AppProperties properties = new AppProperties();
        properties.getAuth().getFirebase().setRecentLoginSeconds(300);
        properties.getAuth().getFirebase().setSessionLifetimeSeconds(604800);
        service = new FirebaseSessionService(gateway, provisioningService, cookieService, profileService, properties);
    }

    @Test
    void createsSessionForRecentVerifiedIdentity() {
        FirebaseIdentity identity = identity(Instant.now());
        User user = User.builder().id(UUID.randomUUID()).profile(new Profile()).build();
        when(gateway.verifyIdToken("id-token", true)).thenReturn(identity);
        when(provisioningService.resolveOrCreate(identity)).thenReturn(user);
        when(gateway.createSessionCookie("id-token", Duration.ofDays(7))).thenReturn("session-cookie");

        service.createSession("id-token", response);

        verify(cookieService).write(response, "session-cookie");
        verify(profileService).updateLoginStreak(user.getProfile());
    }

    @Test
    void rejectsStaleAuthenticationBeforeProvisioning() {
        FirebaseIdentity identity = identity(Instant.now().minusSeconds(301));
        when(gateway.verifyIdToken("id-token", true)).thenReturn(identity);

        assertThatThrownBy(() -> service.createSession("id-token", response))
                .isInstanceOf(FirebaseAuthenticationException.class)
                .hasMessageContaining("Recent");

        verifyNoInteractions(provisioningService, cookieService, profileService);
    }

    @Test
    void refreshesSessionOnlyForTheCurrentFirebaseIdentity() {
        FirebaseIdentity identity = identity(Instant.now());
        User user = User.builder().id(UUID.randomUUID()).build();
        var principal = new com.almonium.auth.firebase.security.FirebasePrincipal(
                "firebase-uid", user.getId(), "user@example.com", Instant.now().minusSeconds(600), "google.com");
        when(gateway.verifyIdToken("id-token", true)).thenReturn(identity);
        when(gateway.createSessionCookie("id-token", Duration.ofDays(7))).thenReturn("session-cookie");

        service.reauthenticateSession("id-token", principal, user, response);

        verify(cookieService).write(response, "session-cookie");
        verifyNoInteractions(provisioningService, profileService);
    }

    @Test
    void rejectsReauthenticationAsAnotherFirebaseIdentity() {
        FirebaseIdentity identity =
                new FirebaseIdentity("other-firebase-uid", "other@example.com", true, Instant.now(), "google.com");
        User user = User.builder().id(UUID.randomUUID()).build();
        var principal = new com.almonium.auth.firebase.security.FirebasePrincipal(
                "firebase-uid", user.getId(), "user@example.com", Instant.now().minusSeconds(600), "google.com");
        when(gateway.verifyIdToken("id-token", true)).thenReturn(identity);

        assertThatThrownBy(() -> service.reauthenticateSession("id-token", principal, user, response))
                .isInstanceOf(FirebaseAuthenticationException.class)
                .hasMessageContaining("currently signed-in account");

        verify(gateway, never())
                .createSessionCookie(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(cookieService, provisioningService, profileService);
    }

    private FirebaseIdentity identity(Instant authTime) {
        return new FirebaseIdentity("firebase-uid", "user@example.com", true, authTime, "password");
    }
}
