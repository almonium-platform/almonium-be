package com.almonium.auth.firebase.gateway;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.model.FirebaseAccountSummary;
import com.almonium.auth.firebase.model.FirebaseAuthProvider;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestFirebaseAuthGateway implements FirebaseAuthGateway {
    private FirebaseAuthenticationException unavailable() {
        return new FirebaseAuthenticationException("Firebase is unavailable in the test profile");
    }

    @Override
    public FirebaseIdentity verifyIdToken(String idToken, boolean checkRevoked) {
        throw unavailable();
    }

    @Override
    public String createSessionCookie(String idToken, Duration lifetime) {
        throw unavailable();
    }

    @Override
    public FirebaseIdentity verifySessionCookie(String sessionCookie, boolean checkRevoked) {
        throw unavailable();
    }

    @Override
    public List<FirebaseAuthProvider> getAuthProviders(String firebaseUid) {
        throw unavailable();
    }

    @Override
    public boolean accountExistsByEmail(String email) {
        throw unavailable();
    }

    @Override
    public Optional<String> generatePasswordResetLink(String email, String continueUrl) {
        throw unavailable();
    }

    @Override
    public String generateEmailVerificationLink(String email, String continueUrl) {
        throw unavailable();
    }

    @Override
    public void updateEmail(String firebaseUid, String email) {
        throw unavailable();
    }

    @Override
    public void deleteUser(String firebaseUid) {
        throw unavailable();
    }

    @Override
    public void markEmailVerified(String firebaseUid) {
        throw unavailable();
    }

    @Override
    public Optional<FirebaseAccountSummary> findAccountByEmail(String email) {
        throw unavailable();
    }

    @Override
    public String projectId() {
        throw unavailable();
    }

    @Override
    public List<String> listAllUserIds() {
        throw unavailable();
    }

    @Override
    public void deleteUsers(List<String> firebaseUids) {
        throw unavailable();
    }
}
