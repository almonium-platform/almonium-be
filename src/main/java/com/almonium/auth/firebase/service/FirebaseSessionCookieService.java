package com.almonium.auth.firebase.service;

import com.almonium.config.properties.AppProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class FirebaseSessionCookieService {
    public static final String COOKIE_NAME = "firebaseSession";

    private final AppProperties appProperties;

    public FirebaseSessionCookieService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public Optional<String> read(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public void write(HttpServletResponse response, String value) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie(
                                value,
                                Duration.ofSeconds(
                                        appProperties.getAuth().getFirebase().getSessionLifetimeSeconds()))
                        .toString());
    }

    public void clear(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie("", Duration.ZERO).toString());
    }

    private ResponseCookie cookie(String value, Duration maxAge) {
        String host = configuredApiHost();
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(!"localhost".equalsIgnoreCase(host))
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax");
        if (host.endsWith(".almonium.com") || "almonium.com".equals(host)) {
            builder.domain("almonium.com");
        }
        return builder.build();
    }

    private String configuredApiHost() {
        String apiDomain = appProperties.getApiDomain();
        URI uri = URI.create(apiDomain.contains("://") ? apiDomain : "https://" + apiDomain);
        return Optional.ofNullable(uri.getHost()).orElse("localhost");
    }
}
