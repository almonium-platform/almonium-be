package linguarium.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import linguarium.auth.common.model.entity.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private static final String TOKEN_EXPIRED = "Expired JWT token";
    private static final String TOKEN_INVALID = "Invalid JWT token";
    private static final String TOKEN_UNSUPPORTED = "Unsupported JWT token";
    private static final String TOKEN_SIGNATURE_INVALID = "Invalid JWT signature";
    private static final String TOKEN_CLAIMS_EMPTY = "JWT claims string is empty.";

    @Value("${app.auth.tokenSigningSecret}")
    private String tokenSecret;

    @Value("${app.auth.tokenExpirationDurationMillis}")
    private long tokenExpirationMSec;

    public String createToken(Authentication authentication) {
        Long id = getIdFromAuthentication(authentication);
        Instant now = Instant.now();
        LocalDateTime expiryDateTime =
                LocalDateTime.ofInstant(now.plusMillis(tokenExpirationMSec), ZoneId.systemDefault());

        return Jwts.builder()
                .setSubject(Long.toString(id))
                .setIssuedAt(Date.from(now))
                .setExpiration(
                        Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims =
                Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();

        return Long.parseLong(claims.getSubject());
    }

    protected boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(authToken);
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

    private static Long getIdFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Principal) {
            return ((Principal) authentication.getPrincipal()).getId();
        }
        throw new IllegalStateException("Authentication principle of unknown type! " + principal.getClass());
    }
}
