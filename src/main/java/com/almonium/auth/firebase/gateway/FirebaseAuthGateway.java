package com.almonium.auth.firebase.gateway;

import com.almonium.auth.firebase.model.FirebaseAccountSummary;
import com.almonium.auth.firebase.model.FirebaseAuthProvider;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface FirebaseAuthGateway {
    FirebaseIdentity verifyIdToken(String idToken, boolean checkRevoked);

    String createSessionCookie(String idToken, Duration lifetime);

    FirebaseIdentity verifySessionCookie(String sessionCookie, boolean checkRevoked);

    List<FirebaseAuthProvider> getAuthProviders(String firebaseUid);

    boolean accountExistsByEmail(String email);

    Optional<String> generatePasswordResetLink(String email, String continueUrl);

    String generateEmailVerificationLink(String email, String continueUrl);

    void updateEmail(String firebaseUid, String email);

    void deleteUser(String firebaseUid);

    /** Writes the conclusion back to Firebase, so the next token carries it. */
    void markEmailVerified(String firebaseUid);

    Optional<FirebaseAccountSummary> findAccountByEmail(String email);

    /** The Firebase project these credentials point at: the blast radius of anything destructive. */
    String projectId();

    List<String> listAllUserIds();

    void deleteUsers(List<String> firebaseUids);
}
