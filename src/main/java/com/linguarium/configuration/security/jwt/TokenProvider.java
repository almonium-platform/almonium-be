package com.linguarium.configuration.security.jwt;

import com.linguarium.auth.model.LocalUser;
import com.linguarium.configuration.AuthenticationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenProvider {
    private static final String TOKEN_EXPIRED = "Expired JWT token";
    private static final String TOKEN_INVALID = "Invalid JWT token";
    private static final String TOKEN_UNSUPPORTED = "Unsupported JWT token";
    private static final String TOKEN_SIGNATURE_INVALID = "Invalid JWT signature";
    private static final String TOKEN_CLAIMS_EMPTY = "JWT claims string is empty.";

    AuthenticationProperties authenticationProperties;

    public String createToken(Authentication authentication) {
        LocalUser userPrincipal = (LocalUser) authentication.getPrincipal();

        Instant now = Instant.now();
        long expirationMillis = authenticationProperties.getAuth().getTokenExpirationMSec();
        LocalDateTime expiryDateTime = LocalDateTime.ofInstant(
                now.plusMillis(expirationMillis),
                ZoneId.systemDefault());

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getUser().getId()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, authenticationProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(authenticationProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(authenticationProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error(TOKEN_SIGNATURE_INVALID);
        } catch (MalformedJwtException ex) {
            log.error(TOKEN_INVALID);
        } catch (ExpiredJwtException ex) {
            log.error(TOKEN_EXPIRED);
        } catch (UnsupportedJwtException ex) {
            log.error(TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException ex) {
            log.error(TOKEN_CLAIMS_EMPTY);
        }
        return false;
    }
}
