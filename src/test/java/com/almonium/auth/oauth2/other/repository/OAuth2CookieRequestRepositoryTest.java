package com.almonium.auth.oauth2.other.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

class OAuth2CookieRequestRepositoryTest {
    private static final Instant NOW = Instant.parse("2026-07-18T12:00:00Z");
    private static final String STATE = "oauth-state";

    private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);
    private final OAuth2CookieRequestRepository repository = new OAuth2CookieRequestRepository(clock);

    @Test
    void saveStoresAuthorizationRequestOnServerWithoutAuthorizationRequestCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        repository.saveAuthorizationRequest(authorizationRequest(), request, response);

        assertThat(request.getSession(false)).isNotNull();
        assertThat(response.getHeaders("Set-Cookie")).noneMatch(header -> header.startsWith("oauth2_auth_request="));
    }

    @Test
    void loadRequiresMatchingState() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        OAuth2AuthorizationRequest authorizationRequest = authorizationRequest();
        repository.saveAuthorizationRequest(authorizationRequest, request, new MockHttpServletResponse());

        request.setParameter("state", "modified-state");
        assertThat(repository.loadAuthorizationRequest(request)).isNull();

        request.setParameter("state", STATE);
        assertThat(repository.loadAuthorizationRequest(request)).isSameAs(authorizationRequest);
    }

    @Test
    void removeConsumesAuthorizationRequestOnce() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        OAuth2AuthorizationRequest authorizationRequest = authorizationRequest();
        repository.saveAuthorizationRequest(authorizationRequest, request, response);
        request.setParameter("state", STATE);

        assertThat(repository.removeAuthorizationRequest(request, response)).isSameAs(authorizationRequest);
        assertThat(repository.removeAuthorizationRequest(request, response)).isNull();
    }

    @Test
    void loadIgnoresLegacySerializedAuthorizationRequestCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("oauth2_auth_request", "rO0ABXNy-malicious-input"));
        request.setParameter("state", STATE);

        assertThat(repository.loadAuthorizationRequest(request)).isNull();
    }

    @Test
    void loadRejectsExpiredAuthorizationRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        repository.saveAuthorizationRequest(authorizationRequest(), request, new MockHttpServletResponse());
        request.setParameter("state", STATE);

        OAuth2CookieRequestRepository expiredRepository =
                new OAuth2CookieRequestRepository(Clock.fixed(NOW.plusSeconds(180), ZoneOffset.UTC));

        assertThat(expiredRepository.loadAuthorizationRequest(request)).isNull();
    }

    private OAuth2AuthorizationRequest authorizationRequest() {
        return OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri("https://provider.example/authorize")
                .clientId("client-id")
                .redirectUri("https://backend.example/oauth/callback")
                .state(STATE)
                .build();
    }
}
