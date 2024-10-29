package com.almonium.auth.oauth2.other.repository;

import static com.almonium.auth.common.util.CookieUtil.INTENT_PARAM_COOKIE_NAME;
import static com.almonium.auth.common.util.CookieUtil.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.common.util.UrlUtil;
import com.almonium.auth.oauth2.other.model.enums.OAuth2Intent;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class OAuth2CookieRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtil.deserialize(cookie.getValue(), OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtil.deleteCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtil.deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
            CookieUtil.deleteCookie(response, INTENT_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtil.addCookie(
                response,
                OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtil.serialize(authorizationRequest),
                COOKIE_EXPIRE_SECONDS);

        String intentParam = request.getParameter(INTENT_PARAM_COOKIE_NAME);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        OAuth2Intent intent = OAuth2Intent.fromString(intentParam);

        if (intent != null) {
            CookieUtil.addCookie(response, INTENT_PARAM_COOKIE_NAME, intentParam, COOKIE_EXPIRE_SECONDS);
            if (intent == OAuth2Intent.LINK) {
                // Front-end will use this to show an appropriate success message
                redirectUriAfterLogin = UrlUtil.addQueryParam(redirectUriAfterLogin, "linked", "true");
            }
        }

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtil.addCookie(
                    response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request, HttpServletResponse response) {
        return loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletResponse response) {
        CookieUtil.deleteCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtil.deleteCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME);
        CookieUtil.deleteCookie(response, INTENT_PARAM_COOKIE_NAME);
    }
}
