package com.almonium.auth.firebase.gateway;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.exception.FirebaseIdentityManagementException;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class FirebaseAdminAuthGateway implements FirebaseAuthGateway {
    private final FirebaseAuth firebaseAuth;

    public FirebaseAdminAuthGateway(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public FirebaseIdentity verifyIdToken(String idToken, boolean checkRevoked) {
        try {
            return toIdentity(firebaseAuth.verifyIdToken(idToken, checkRevoked));
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseAuthenticationException("Invalid Firebase ID token", exception);
        }
    }

    @Override
    public String createSessionCookie(String idToken, Duration lifetime) {
        try {
            SessionCookieOptions options = SessionCookieOptions.builder()
                    .setExpiresIn(lifetime.toMillis())
                    .build();
            return firebaseAuth.createSessionCookie(idToken, options);
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseAuthenticationException("Unable to create Firebase session", exception);
        }
    }

    @Override
    public FirebaseIdentity verifySessionCookie(String sessionCookie, boolean checkRevoked) {
        try {
            return toIdentity(firebaseAuth.verifySessionCookie(sessionCookie, checkRevoked));
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseAuthenticationException("Invalid Firebase session", exception);
        }
    }

    @Override
    public void deleteUser(String firebaseUid) {
        try {
            firebaseAuth.deleteUser(firebaseUid);
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to delete Firebase user", exception);
        }
    }

    private FirebaseIdentity toIdentity(FirebaseToken token) {
        Map<String, Object> claims = token.getClaims();
        Object authTime = claims.get("auth_time");
        if (!(authTime instanceof Number authTimeSeconds)) {
            throw new FirebaseAuthenticationException("Firebase token is missing auth_time");
        }

        String signInProvider = null;
        Object firebaseClaim = claims.get("firebase");
        if (firebaseClaim instanceof Map<?, ?> firebaseClaims) {
            Object provider = firebaseClaims.get("sign_in_provider");
            signInProvider = provider == null ? null : provider.toString();
        }

        return new FirebaseIdentity(
                token.getUid(),
                token.getEmail(),
                token.isEmailVerified(),
                Instant.ofEpochSecond(authTimeSeconds.longValue()),
                signInProvider);
    }
}
