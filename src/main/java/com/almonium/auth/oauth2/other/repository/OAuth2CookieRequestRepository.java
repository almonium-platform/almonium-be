package com.almonium.auth.oauth2.other.repository;

import static com.almonium.auth.common.util.CookieUtil.INTENT_PARAM_COOKIE_NAME;
import static com.almonium.auth.common.util.CookieUtil.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.almonium.auth.common.util.CookieUtil.USER_ID_PARAM_COOKIE_NAME;

import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.common.util.UrlUtil;
import com.almonium.auth.oauth2.other.model.enums.OAuth2Intent;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class OAuth2CookieRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private static final int COOKIE_EXPIRE_SECONDS = 180;
    private static final String LEGACY_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    private static final Duration AUTHORIZATION_REQUEST_LIFETIME = Duration.ofSeconds(COOKIE_EXPIRE_SECONDS);
    private static final String AUTHORIZATION_REQUEST_SESSION_ATTRIBUTE =
            OAuth2CookieRequestRepository.class.getName() + ".AUTHORIZATION_REQUEST";

    private final Clock clock;

    public OAuth2CookieRequestRepository() {
        this(Clock.systemUTC());
    }

    OAuth2CookieRequestRepository(Clock clock) {
        this.clock = clock;
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        OAuth2AuthorizationAttempt attempt =
                (OAuth2AuthorizationAttempt) session.getAttribute(AUTHORIZATION_REQUEST_SESSION_ATTRIBUTE);
        if (attempt == null) {
            return null;
        }

        if (!attempt.expiresAt().isAfter(clock.instant())) {
            session.removeAttribute(AUTHORIZATION_REQUEST_SESSION_ATTRIBUTE);
            return null;
        }

        String state = request.getParameter("state");
        return attempt.authorizationRequest().getState().equals(state) ? attempt.authorizationRequest() : null;
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeStoredAuthorizationRequest(request);
            CookieUtil.deleteCookie(response, LEGACY_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtil.deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
            CookieUtil.deleteCookie(response, INTENT_PARAM_COOKIE_NAME);
            return;
        }

        request.getSession()
                .setAttribute(
                        AUTHORIZATION_REQUEST_SESSION_ATTRIBUTE,
                        new OAuth2AuthorizationAttempt(
                                authorizationRequest, clock.instant().plus(AUTHORIZATION_REQUEST_LIFETIME)));

        String intentParam = request.getParameter(INTENT_PARAM_COOKIE_NAME);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        OAuth2Intent intent = OAuth2Intent.fromString(intentParam);

        if (intent != null) {
            CookieUtil.addCookie(response, INTENT_PARAM_COOKIE_NAME, intentParam, COOKIE_EXPIRE_SECONDS);
            if (intent == OAuth2Intent.REAUTH) {
                String userIdParam = request.getParameter(USER_ID_PARAM_COOKIE_NAME);
                CookieUtil.addCookie(response, USER_ID_PARAM_COOKIE_NAME, userIdParam, COOKIE_EXPIRE_SECONDS);
            }
            redirectUriAfterLogin = UrlUtil.addQueryParam(
                    redirectUriAfterLogin,
                    INTENT_PARAM_COOKIE_NAME,
                    intent.name().toLowerCase());
        }

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtil.addCookie(
                    response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
        if (authorizationRequest != null) {
            removeStoredAuthorizationRequest(request);
        }
        return authorizationRequest;
    }

    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        CookieUtil.deleteCookie(response, LEGACY_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtil.deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
        CookieUtil.deleteCookie(response, INTENT_PARAM_COOKIE_NAME);
    }

    private void removeStoredAuthorizationRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(AUTHORIZATION_REQUEST_SESSION_ATTRIBUTE);
        }
    }

    private record OAuth2AuthorizationAttempt(OAuth2AuthorizationRequest authorizationRequest, Instant expiresAt) {}
}
