package com.almonium.auth.firebase.gateway;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.exception.FirebaseIdentityManagementException;
import com.almonium.auth.firebase.model.FirebaseAuthProvider;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserRecord;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public List<FirebaseAuthProvider> getAuthProviders(String firebaseUid) {
        try {
            UserRecord user = firebaseAuth.getUser(firebaseUid);
            String createdAt = Instant.ofEpochMilli(user.getUserMetadata().getCreationTimestamp())
                    .toString();
            String updatedAt = Instant.ofEpochMilli(user.getUserMetadata().getLastSignInTimestamp())
                    .toString();
            return Arrays.stream(user.getProviderData())
                    .map(provider -> toAuthProvider(provider, createdAt, updatedAt))
                    .toList();
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException(
                    "Unable to load Firebase authentication providers", exception);
        }
    }

    @Override
    public boolean accountExistsByEmail(String email) {
        try {
            firebaseAuth.getUserByEmail(email);
            return true;
        } catch (FirebaseAuthException exception) {
            if (exception.getAuthErrorCode() == AuthErrorCode.USER_NOT_FOUND) {
                return false;
            }
            throw new FirebaseIdentityManagementException("Unable to look up Firebase account", exception);
        } catch (IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to look up Firebase account", exception);
        }
    }

    @Override
    public Optional<String> generatePasswordResetLink(String email, String continueUrl) {
        try {
            return Optional.of(firebaseAuth.generatePasswordResetLink(email, actionCodeSettings(continueUrl)));
        } catch (FirebaseAuthException exception) {
            if (exception.getAuthErrorCode() == AuthErrorCode.USER_NOT_FOUND) {
                return Optional.empty();
            }
            throw new FirebaseIdentityManagementException("Unable to generate password reset link", exception);
        } catch (IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to generate password reset link", exception);
        }
    }

    @Override
    public String generateEmailVerificationLink(String email, String continueUrl) {
        try {
            return firebaseAuth.generateEmailVerificationLink(email, actionCodeSettings(continueUrl));
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to generate email verification link", exception);
        }
    }

    @Override
    public void updateEmail(String firebaseUid, String email) {
        try {
            firebaseAuth.updateUser(
                    new UserRecord.UpdateRequest(firebaseUid).setEmail(email).setEmailVerified(true));
            firebaseAuth.revokeRefreshTokens(firebaseUid);
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to update Firebase email", exception);
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
                signInProvider,
                Boolean.TRUE.equals(claims.get("admin")));
    }

    private FirebaseAuthProvider toAuthProvider(UserInfo provider, String createdAt, String updatedAt) {
        String providerId = provider.getProviderId();
        String normalizedProvider = "password".equals(providerId) ? "local" : providerId.replace(".com", "");
        return new FirebaseAuthProvider(normalizedProvider, provider.getEmail(), createdAt, updatedAt);
    }

    private ActionCodeSettings actionCodeSettings(String continueUrl) {
        return ActionCodeSettings.builder().setUrl(continueUrl).build();
    }
}
