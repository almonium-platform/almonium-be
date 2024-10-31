package com.almonium.auth.token.filter;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.service.impl.AuthTokenService;
import com.almonium.auth.token.util.BearerTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    AuthTokenService authTokenService;

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().contains("/public")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearer = BearerTokenUtil.getBearerTokenFromRequest(request);

        Optional<String> token =
                Optional.ofNullable(bearer).filter(StringUtils::hasText).filter(authTokenService::validateToken);

        if (token.isEmpty()) {
            token = CookieUtil.getCookie(request, CookieUtil.ACCESS_TOKEN_COOKIE_NAME)
                    .map(Cookie::getValue)
                    .filter(authTokenService::validateToken);
        }

        token.ifPresent(validToken -> SecurityContextHolder.getContext()
                .setAuthentication(authTokenService.getAuthenticationFromToken(validToken)));

        filterChain.doFilter(request, response);
    }
}
