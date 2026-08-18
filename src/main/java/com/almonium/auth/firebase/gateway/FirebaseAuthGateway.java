package com.almonium.auth.firebase.gateway;

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

    Optional<String> generatePasswordResetLink(String email, String continueUrl);

    String generateEmailVerificationLink(String email, String continueUrl);

    void updateEmail(String firebaseUid, String email);

    void deleteUser(String firebaseUid);
}
