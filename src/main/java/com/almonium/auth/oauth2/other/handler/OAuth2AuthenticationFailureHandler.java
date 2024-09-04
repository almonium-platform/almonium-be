package com.almonium.auth.oauth2.other.handler;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.oauth2.other.repository.OAuth2CookieRequestRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    OAuth2CookieRequestRepository requestRepository;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        String targetUrl = CookieUtil.getCookie(request, CookieUtil.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("/");

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build()
                .toUriString();

        requestRepository.removeAuthorizationRequestCookies(response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
