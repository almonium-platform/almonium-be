package com.almonium.auth.token.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.exception.AuthMethodNotFoundException;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.common.util.CookieUtil;
import com.almonium.auth.token.model.entity.RefreshToken;
import com.almonium.auth.token.repository.RefreshTokenRepository;
import com.almonium.config.properties.AppProperties;
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
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthTokenService {
    private static final String LOCALHOST = "localhost";
    private static final String IS_LIVE_TOKEN_CLAIM = "isLive";

    AppProperties appProperties;
    PrincipalRepository principalRepository;
    RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void revokeRefreshTokensByUser(User user) {
        // here, we could mark the refresh token as revoked instead of deleting it
        refreshTokenRepository.deleteAllByUser(user);
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
        CookieUtil.deleteCookie(response, CookieUtil.ACCESS_TOKEN_COOKIE_NAME, getCleanBackendDomain());
        CookieUtil.deleteCookie(
                response,
                CookieUtil.REFRESH_TOKEN_COOKIE_NAME,
                appProperties.getAuth().getJwt().getRefreshToken().getUrl(),
                getCleanBackendDomain()); // Refresh token cleared with path
    }

    public String createAndSetAccessTokenForLiveLogin(Authentication authentication, HttpServletResponse response) {
        String accessToken = generateLiveAccessToken(authentication);
        CookieUtil.addCookie(
                response,
                CookieUtil.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                appProperties.getAuth().getJwt().getAccessToken().getLifetime(),
                getCleanBackendDomain());
        return accessToken;
    }

    public String createAndSetAccessTokenForRefresh(Authentication authentication, HttpServletResponse response) {
        String accessToken = generateRefreshedAccessToken(authentication);
        CookieUtil.addCookie(
                response,
                CookieUtil.ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                appProperties.getAuth().getJwt().getAccessToken().getLifetime(),
                getCleanBackendDomain());
        return accessToken;
    }

    public Authentication getAuthenticationFromToken(String token) {
        Principal principal = getPrincipalFromAccessToken(token);

        return new UsernamePasswordAuthenticationToken(principal, null, Principal.ROLES);
    }

    public String createAndSetRefreshToken(Authentication authentication, HttpServletResponse response) {
        String refreshToken = createRefreshToken(authentication);

        CookieUtil.addCookieWithPath(
                response,
                CookieUtil.REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                appProperties.getAuth().getJwt().getRefreshToken().getUrl(),
                appProperties.getAuth().getJwt().getRefreshToken().getLifetime(),
                getCleanBackendDomain());

        return refreshToken;
    }

    public boolean isAccessTokenRefreshed(String accessToken) {
        Claims claims = extractClaims(accessToken);
        Object isLiveClaim = claims.get(IS_LIVE_TOKEN_CLAIM);
        return !(isLiveClaim instanceof Boolean && (Boolean) isLiveClaim);
    }

    public Optional<Instant> recentLoginPrivilegeExpiresAt(String accessToken) {
        if (isAccessTokenRefreshed(accessToken)) {
            return Optional.empty();
        }
        return Optional.of(extractClaims(accessToken).getExpiration().toInstant());
    }

    private String generateLiveAccessToken(Authentication authentication) {
        return generateToken(
                authentication,
                appProperties.getAuth().getJwt().getAccessToken().getLifetime(),
                true);
    }

    private String generateRefreshedAccessToken(Authentication authentication) {
        return generateToken(
                authentication,
                appProperties.getAuth().getJwt().getAccessToken().getLifetime(),
                false);
    }

    private Principal getPrincipalFromAccessToken(String token) {
        Claims claims = extractClaims(token);
        long id = Long.parseLong(claims.getSubject());

        return principalRepository
                .findById(id)
                .orElseThrow(() -> new AuthMethodNotFoundException("Principal not found by id: " + id));
    }

    private String getCleanBackendDomain() {
        String backendDomain = appProperties.getApiDomain();
        return backendDomain.contains(LOCALHOST) ? LOCALHOST : backendDomain;
    }

    private String createRefreshToken(Authentication authentication) {
        String token = generateToken(
                authentication,
                appProperties.getAuth().getJwt().getRefreshToken().getLifetime(),
                false);
        Claims claims = extractClaims(token);
        Instant issueDate = claims.getIssuedAt().toInstant();
        Instant expiryDate = claims.getExpiration().toInstant();
        UUID id = UUID.fromString(claims.getId());
        Principal principal = getPrincipalFromAccessToken(token);
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

    private String generateToken(
            Authentication authentication, long tokenExpirationSeconds, boolean isReauthenticated) {
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
                .claim(IS_LIVE_TOKEN_CLAIM, isReauthenticated)
                .issuedAt(Date.from(now))
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(appProperties.getAuth().getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
