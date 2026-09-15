package com.almonium.auth.firebase.gateway;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.exception.FirebaseIdentityManagementException;
import com.almonium.auth.firebase.model.FirebaseAccountSummary;
import com.almonium.auth.firebase.model.FirebaseAuthProvider;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.DeleteUsersResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.SessionCookieOptions;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserRecord;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test")
public class FirebaseAdminAuthGateway implements FirebaseAuthGateway {
    // Firebase deletes at most 1000 accounts per call.
    private static final int DELETE_BATCH_SIZE = 1000;

    private final FirebaseAuth firebaseAuth;
    private final FirebaseApp firebaseApp;

    public FirebaseAdminAuthGateway(FirebaseAuth firebaseAuth, FirebaseApp firebaseApp) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseApp = firebaseApp;
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
            return Arrays.stream(user.getProviderData())
                    .map(this::toAuthProvider)
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

    @Override
    public void markEmailVerified(String firebaseUid) {
        try {
            firebaseAuth.updateUser(new UserRecord.UpdateRequest(firebaseUid).setEmailVerified(true));
            log.info("Marked Firebase user {} as email-verified on a linked provider's word", firebaseUid);
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to mark the Firebase email verified", exception);
        }
    }

    @Override
    public Optional<FirebaseAccountSummary> findAccountByEmail(String email) {
        try {
            UserRecord user = firebaseAuth.getUserByEmail(email);
            return Optional.of(new FirebaseAccountSummary(
                    user.getUid(),
                    user.getEmail(),
                    user.isEmailVerified(),
                    Arrays.stream(user.getProviderData())
                            .map(this::toAuthProvider)
                            .toList()));
        } catch (FirebaseAuthException exception) {
            if (exception.getAuthErrorCode() == AuthErrorCode.USER_NOT_FOUND) {
                return Optional.empty();
            }
            throw new FirebaseIdentityManagementException("Unable to look up Firebase account", exception);
        } catch (IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to look up Firebase account", exception);
        }
    }

    @Override
    public String projectId() {
        String projectId = firebaseApp.getOptions().getProjectId();
        if (projectId == null || projectId.isBlank()) {
            throw new FirebaseIdentityManagementException("Firebase credentials do not name a project", null);
        }
        return projectId;
    }

    @Override
    public List<String> listAllUserIds() {
        try {
            List<String> uids = new ArrayList<>();
            firebaseAuth.listUsers(null).iterateAll().forEach(user -> uids.add(user.getUid()));
            return uids;
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to list Firebase users", exception);
        }
    }

    @Override
    public void deleteUsers(List<String> firebaseUids) {
        try {
            for (int from = 0; from < firebaseUids.size(); from += DELETE_BATCH_SIZE) {
                List<String> batch =
                        firebaseUids.subList(from, Math.min(from + DELETE_BATCH_SIZE, firebaseUids.size()));
                DeleteUsersResult result = firebaseAuth.deleteUsers(batch);
                result.getErrors()
                        .forEach(error -> log.error(
                                "Firebase refused to delete {}: {}", batch.get(error.getIndex()), error.getReason()));
            }
        } catch (FirebaseAuthException | IllegalArgumentException exception) {
            throw new FirebaseIdentityManagementException("Unable to delete Firebase users", exception);
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

    private FirebaseAuthProvider toAuthProvider(UserInfo provider) {
        String providerId = provider.getProviderId();
        String normalizedProvider = "password".equals(providerId) ? "local" : providerId.replace(".com", "");
        return new FirebaseAuthProvider(normalizedProvider, provider.getEmail());
    }

    private ActionCodeSettings actionCodeSettings(String continueUrl) {
        return ActionCodeSettings.builder().setUrl(continueUrl).build();
    }
}
