package com.almonium.auth.firebase;

import static com.almonium.auth.firebase.service.FirebaseSessionCookieService.COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.almonium.auth.common.controller.open.CsrfController;
import com.almonium.auth.firebase.controller.FirebaseSessionController;
import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.filter.FirebaseSessionAuthenticationFilter;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseAuthProvider;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.auth.firebase.service.FirebaseSessionCookieService;
import com.almonium.auth.firebase.service.FirebaseSessionService;
import com.almonium.auth.firebase.service.FirebaseUserProvisioningService;
import com.almonium.config.WebMvcConfig;
import com.almonium.config.aspect.RecentLoginAspect;
import com.almonium.config.security.WebSecurityConfig;
import com.almonium.user.core.factory.UserRegistrationService;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.ProfileService;
import com.almonium.user.core.service.UserService;
import com.almonium.util.config.TestConfig;
import jakarta.servlet.http.Cookie;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = {FirebaseSessionController.class, CsrfController.class})
@Import({
    WebSecurityConfig.class,
    WebMvcConfig.class,
    FirebaseSessionAuthenticationFilter.class,
    FirebaseSessionCookieService.class,
    FirebaseSessionService.class,
    FirebaseUserProvisioningService.class,
    RecentLoginAspect.class,
    TestConfig.class,
    AopAutoConfiguration.class
})
class FirebaseSecurityIntegrationTest {
    private static final String UID = "firebase-uid";
    private static final String EMAIL = "user@example.com";
    private static final String SESSION_COOKIE = "verified-session";
    private static final String ID_TOKEN = "verified-id-token";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FirebaseAuthGateway firebaseAuthGateway;

    @MockitoBean
    UserRepository userRepository;

    @MockitoBean
    UserRegistrationService userRegistrationService;

    @MockitoBean
    ProfileService profileService;

    @MockitoBean
    UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        Profile profile = new Profile();
        user = User.builder()
                .id(UUID.randomUUID())
                .firebaseUid(UID)
                .email(EMAIL)
                .emailVerified(true)
                .profile(profile)
                .build();
    }

    @Test
    void csrfEndpointIssuesCookieAndProtectedSessionExchangeRequiresMatchingToken() throws Exception {
        stubSuccessfulSessionExchange();

        MvcResult csrfResult = mockMvc.perform(get("/public/csrf/token"))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("XSRF-TOKEN"))
                .andReturn();

        Cookie csrfCookie = csrfResult.getResponse().getCookie("XSRF-TOKEN");
        assertThat(csrfCookie).isNotNull();

        mockMvc.perform(post("/auth/session").contentType("application/json").content("{\"idToken\":\"id-token\"}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/auth/session")
                        .cookie(csrfCookie)
                        .header("X-XSRF-TOKEN", csrfCookie.getValue())
                        .contentType("application/json")
                        .content("{\"idToken\":\"id-token\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void sessionExchangeWritesHardenedCookie() throws Exception {
        stubSuccessfulSessionExchange();

        mockMvc.perform(
                        post("/auth/session")
                                .with(org.springframework.security.test.web.servlet.request
                                        .SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType("application/json")
                                .content("{\"idToken\":\"id-token\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                                HttpHeaders.SET_COOKIE,
                                org.hamcrest.Matchers.allOf(
                                        org.hamcrest.Matchers.containsString(COOKIE_NAME + "=" + SESSION_COOKIE),
                                        org.hamcrest.Matchers.containsString("Path=/"),
                                        org.hamcrest.Matchers.containsString("Max-Age=604800"),
                                        org.hamcrest.Matchers.containsString("Secure"),
                                        org.hamcrest.Matchers.containsString("HttpOnly"),
                                        org.hamcrest.Matchers.containsString("SameSite=Lax"))));
    }

    @Test
    void logoutClearsSessionCookieWithoutRequiringAuthentication() throws Exception {
        mockMvc.perform(
                        post("/auth/session/logout")
                                .with(org.springframework.security.test.web.servlet.request
                                        .SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent())
                .andExpect(header().string(
                                HttpHeaders.SET_COOKIE,
                                org.hamcrest.Matchers.allOf(
                                        org.hamcrest.Matchers.containsString(COOKIE_NAME + "="),
                                        org.hamcrest.Matchers.containsString("Max-Age=0"),
                                        org.hamcrest.Matchers.containsString("HttpOnly"))));
    }

    @Test
    void mutatingBearerRequestDoesNotRequireBrowserCsrfToken() throws Exception {
        stubSuccessfulBearerAuthentication(Instant.now());

        mockMvc.perform(post("/auth/session/logout").header(HttpHeaders.AUTHORIZATION, "Bearer " + ID_TOKEN))
                .andExpect(status().isNoContent());

        verify(firebaseAuthGateway).verifyIdToken(ID_TOKEN, false);
    }

    @Test
    void recentLoginAcceptsRevocationCheckedBearerToken() throws Exception {
        stubSuccessfulBearerAuthentication(Instant.now());
        when(firebaseAuthGateway.verifyIdToken(ID_TOKEN, true)).thenReturn(identity(Instant.now()));

        mockMvc.perform(get("/auth/session/recent").header(HttpHeaders.AUTHORIZATION, "Bearer " + ID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(firebaseAuthGateway).verifyIdToken(ID_TOKEN, false);
        verify(firebaseAuthGateway).verifyIdToken(ID_TOKEN, true);
    }

    @Test
    void revokedSessionCannotPassRecentLoginEnforcement() throws Exception {
        FirebaseIdentity identity = identity(Instant.now());
        when(firebaseAuthGateway.verifySessionCookie(SESSION_COOKIE, false)).thenReturn(identity);
        when(firebaseAuthGateway.verifySessionCookie(SESSION_COOKIE, true))
                .thenThrow(new FirebaseAuthenticationException("revoked"));
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/auth/session/recent").cookie(new Cookie(COOKIE_NAME, SESSION_COOKIE)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("within the last 5")));

        verify(firebaseAuthGateway).verifySessionCookie(SESSION_COOKIE, false);
        verify(firebaseAuthGateway).verifySessionCookie(SESSION_COOKIE, true);
    }

    @Test
    void staleSessionCannotPassRecentLoginEnforcement() throws Exception {
        FirebaseIdentity staleIdentity = identity(Instant.now().minusSeconds(301));
        when(firebaseAuthGateway.verifySessionCookie(SESSION_COOKIE, false)).thenReturn(staleIdentity);
        when(firebaseAuthGateway.verifySessionCookie(SESSION_COOKIE, true)).thenReturn(staleIdentity);
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/auth/session/recent").cookie(new Cookie(COOKIE_NAME, SESSION_COOKIE)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void staleAuthenticatedSessionCanListItsReauthenticationProviders() throws Exception {
        FirebaseIdentity staleIdentity = identity(Instant.now().minusSeconds(301));
        when(firebaseAuthGateway.verifySessionCookie(SESSION_COOKIE, false)).thenReturn(staleIdentity);
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.of(user));
        when(firebaseAuthGateway.getAuthProviders(UID))
                .thenReturn(List.of(
                        new FirebaseAuthProvider("google", EMAIL, "2026-01-01T00:00:00Z", "2026-07-01T00:00:00Z")));

        mockMvc.perform(get("/auth/session/providers").cookie(new Cookie(COOKIE_NAME, SESSION_COOKIE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].provider").value("google"))
                .andExpect(jsonPath("$[0].email").value(EMAIL));
    }

    @Test
    void reauthenticationRefreshesOnlyTheCurrentUsersSession() throws Exception {
        FirebaseIdentity staleIdentity = identity(Instant.now().minusSeconds(301));
        FirebaseIdentity freshIdentity = identity(Instant.now());
        when(firebaseAuthGateway.verifySessionCookie(SESSION_COOKIE, false)).thenReturn(staleIdentity);
        when(firebaseAuthGateway.verifyIdToken(ID_TOKEN, true)).thenReturn(freshIdentity);
        when(firebaseAuthGateway.createSessionCookie(ID_TOKEN, Duration.ofDays(7)))
                .thenReturn(SESSION_COOKIE);
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.of(user));
        when(userService.getById(user.getId())).thenReturn(user);

        mockMvc.perform(
                        post("/auth/session/reauth")
                                .with(org.springframework.security.test.web.servlet.request
                                        .SecurityMockMvcRequestPostProcessors.csrf())
                                .cookie(new Cookie(COOKIE_NAME, SESSION_COOKIE))
                                .contentType("application/json")
                                .content("{\"idToken\":\"" + ID_TOKEN + "\"}"))
                .andExpect(status().isOk());

        verify(firebaseAuthGateway).verifyIdToken(ID_TOKEN, true);
        verify(firebaseAuthGateway).createSessionCookie(ID_TOKEN, Duration.ofDays(7));
    }

    @Test
    void reauthenticationRejectsADifferentFirebaseIdentity() throws Exception {
        FirebaseIdentity staleIdentity = identity(Instant.now().minusSeconds(301));
        FirebaseIdentity otherIdentity =
                new FirebaseIdentity("other-uid", "other@example.com", true, Instant.now(), "google.com");
        when(firebaseAuthGateway.verifySessionCookie(SESSION_COOKIE, false)).thenReturn(staleIdentity);
        when(firebaseAuthGateway.verifyIdToken(ID_TOKEN, true)).thenReturn(otherIdentity);
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.of(user));
        when(userService.getById(user.getId())).thenReturn(user);

        mockMvc.perform(
                        post("/auth/session/reauth")
                                .with(org.springframework.security.test.web.servlet.request
                                        .SecurityMockMvcRequestPostProcessors.csrf())
                                .cookie(new Cookie(COOKIE_NAME, SESSION_COOKIE))
                                .contentType("application/json")
                                .content("{\"idToken\":\"" + ID_TOKEN + "\"}"))
                .andExpect(status().isUnauthorized());

        verify(firebaseAuthGateway, never()).createSessionCookie(any(), any(Duration.class));
    }

    @Test
    void provisioningCollisionReturnsConflictWithoutCreatingSessionCookie() throws Exception {
        FirebaseIdentity identity = identity(Instant.now());
        when(firebaseAuthGateway.verifyIdToken("id-token", true)).thenReturn(identity);
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.empty());
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        mockMvc.perform(
                        post("/auth/session")
                                .with(org.springframework.security.test.web.servlet.request
                                        .SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType("application/json")
                                .content("{\"idToken\":\"id-token\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("already uses")))
                .andExpect(cookie().doesNotExist(COOKIE_NAME));

        verify(firebaseAuthGateway, never()).createSessionCookie(any(), any(Duration.class));
        verify(userRegistrationService, never()).createUserWithDefaultPlan(any(), any(Boolean.class), any());
    }

    private void stubSuccessfulSessionExchange() {
        FirebaseIdentity identity = identity(Instant.now());
        when(firebaseAuthGateway.verifyIdToken("id-token", true)).thenReturn(identity);
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(firebaseAuthGateway.createSessionCookie("id-token", Duration.ofDays(7)))
                .thenReturn(SESSION_COOKIE);
    }

    private void stubSuccessfulBearerAuthentication(Instant authenticatedAt) {
        FirebaseIdentity identity = identity(authenticatedAt);
        when(firebaseAuthGateway.verifyIdToken(ID_TOKEN, false)).thenReturn(identity);
        when(userRepository.findByFirebaseUid(UID)).thenReturn(Optional.of(user));
    }

    private FirebaseIdentity identity(Instant authenticatedAt) {
        return new FirebaseIdentity(UID, EMAIL, true, authenticatedAt, "password");
    }
}
