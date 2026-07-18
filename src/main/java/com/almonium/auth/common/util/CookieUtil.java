package com.almonium.auth.common.util;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public class CookieUtil {
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String INTENT_PARAM_COOKIE_NAME = "intent";
    public static final String USER_ID_PARAM_COOKIE_NAME = "userId";
    private static final String EMPTY_PATH = "/";

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        return Optional.ofNullable(request.getCookies()).flatMap(cookies -> Stream.of(cookies)
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst());
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
        addCookieWithPath(response, name, value, EMPTY_PATH, maxAge, domain);
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        addCookieWithPath(response, name, value, EMPTY_PATH, maxAge, null);
    }

    public static void addCookieWithPath(
            HttpServletResponse resp,
            String name,
            String value,
            String path,
            int maxAgeSeconds,
            @Nullable String domain) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path(path)
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .sameSite("Lax");

        if (domain != null) b.domain(domain);
        resp.addHeader(HttpHeaders.SET_COOKIE, b.build().toString());
    }

    public static void deleteCookie(HttpServletResponse response, String name, String domain) {
        deleteCookie(response, name, EMPTY_PATH, domain);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        deleteCookie(response, name, EMPTY_PATH, null);
    }

    public static void deleteCookie(HttpServletResponse response, String name, String path, String domain) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .path(path)
                .maxAge(Duration.ZERO)
                .sameSite("Lax");

        if (domain != null) b.domain(domain);
        response.addHeader(HttpHeaders.SET_COOKIE, b.build().toString());
    }
}
