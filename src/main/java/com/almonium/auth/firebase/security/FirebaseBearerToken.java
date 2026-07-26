package com.almonium.auth.firebase.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.http.HttpHeaders;

public final class FirebaseBearerToken {
    private static final String PREFIX = "Bearer ";

    private FirebaseBearerToken() {}

    public static Optional<String> from(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.regionMatches(true, 0, PREFIX, 0, PREFIX.length())) {
            return Optional.empty();
        }

        String token = authorization.substring(PREFIX.length()).trim();
        return token.isEmpty() ? Optional.empty() : Optional.of(token);
    }
}
