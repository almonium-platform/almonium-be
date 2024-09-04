package com.almonium.auth.token.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.exception.AuthMethodNotFoundException;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.model.entity.RefreshToken;
import com.almonium.auth.token.repository.RefreshTokenRepository;
import com.almonium.user.core.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class AuthTokenService {
    private static final String LOCALHOST = "localhost";
    final PrincipalRepository principalRepository;
    final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.auth.jwt.token-signing-secret}")
    String tokenSecret;

    @Value("${app.auth.jwt.access-token-expiration-duration}")
    int accessTokenExpirationSeconds;

    @Value("${app.auth.jwt.refresh-token-expiration-duration}")
    int refreshTokenExpirationSeconds;

    @Value("${app.auth.jwt.refresh-token-url}")
    String refreshTokenPath;

    @Value("${server.servlet.context-path}")
    String contextPath;

    @Value("${app.api-domain}")
    private String backendDomain;

    String fullRefreshTokenPath;

    public void revokeRefreshTokensByUser(User user) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);
        tokens.forEach(token -> token.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.error("Token validation error: ", ex);
            return false;
        }
    }

    public void clearTokenCookies(HttpServletResponse response) {
        CookieUtil.deleteCookie(
                response,
                CookieUtil.ACCESS_TOKEN_COOKIE_NAME,
                getCleanBackendDomain()); // Access token cleared from root
        CookieUtil.deleteCookie(
                response,
                CookieUtil.REFRESH_TOKEN_COOKIE_NAME,
                getFullRefreshTokenPath(),
                getCleanBackendDomain()); // Refresh token cleared with path
    }

    public String createAndSetAccessToken(Authentication authentication, HttpServletResponse response) {
        String accessToken = createAccessToken(authentication);
        CookieUtil.addCookie(
                response,
                CookieUtil.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                accessTokenExpirationSeconds,
                getCleanBackendDomain());
        return accessToken;
    }

    public String createAndSetRefreshToken(Authentication authentication, HttpServletResponse response) {
        String refreshToken = createRefreshToken(authentication);

        CookieUtil.addCookieWithPath(
                response,
                CookieUtil.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                getFullRefreshTokenPath(),
                refreshTokenExpirationSeconds,
                getCleanBackendDomain());

        return refreshToken;
    }

    public Authentication getAuthenticationFromToken(String token) {
        Principal principal = getPrincipalFromToken(token);

        return new UsernamePasswordAuthenticationToken(
                principal, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public Principal getPrincipalFromToken(String token) {
        Claims claims = extractClaims(token);
        long id = Long.parseLong(claims.getSubject());

        return principalRepository
                .findById(id)
                .orElseThrow(() -> new AuthMethodNotFoundException("Principal not found by id: " + id));
    }

    private String getFullRefreshTokenPath() {
        if (fullRefreshTokenPath == null) {
            fullRefreshTokenPath = contextPath + refreshTokenPath;
        }
        return fullRefreshTokenPath;
    }

    public String getCleanBackendDomain() {
        if (backendDomain.contains(LOCALHOST)) {
            return LOCALHOST;
        }
        return backendDomain;
    }

    private String createAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenExpirationSeconds);
    }

    private String createRefreshToken(Authentication authentication) {
        String token = generateToken(authentication, refreshTokenExpirationSeconds);
        Claims claims = extractClaims(token);
        Instant issueDate = claims.getIssuedAt().toInstant();
        Instant expiryDate = claims.getExpiration().toInstant();
        UUID id = UUID.fromString(claims.getId());
        Principal principal = getPrincipalFromToken(token);
        RefreshToken refreshToken = new RefreshToken(id, principal.getUser(), issueDate, expiryDate);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    private Claims extractClaims(String refreshToken) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();
    }

    private String generateToken(Authentication authentication, long tokenExpirationSeconds) {
        long id = ((Principal) (authentication.getPrincipal())).getId();

        Instant now = Instant.now();
        Date expiryDate = Date.from(now.plusSeconds(tokenExpirationSeconds)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        String jti = UUID.randomUUID().toString();
        SecretKey key = getSecretKey();

        return Jwts.builder()
                .id(jti)
                .subject(Long.toString(id))
                .issuedAt(Date.from(now))
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));
    }
}
