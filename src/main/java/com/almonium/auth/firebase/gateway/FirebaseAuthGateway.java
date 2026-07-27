package com.almonium.auth.firebase.gateway;

import com.almonium.auth.firebase.model.FirebaseAuthProvider;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import java.time.Duration;
import java.util.List;

public interface FirebaseAuthGateway {
    FirebaseIdentity verifyIdToken(String idToken, boolean checkRevoked);

    String createSessionCookie(String idToken, Duration lifetime);

    FirebaseIdentity verifySessionCookie(String sessionCookie, boolean checkRevoked);

    List<FirebaseAuthProvider> getAuthProviders(String firebaseUid);

    void deleteUser(String firebaseUid);
}
