package com.almonium.auth.firebase.gateway;

import com.almonium.auth.firebase.model.FirebaseIdentity;
import java.time.Duration;

public interface FirebaseAuthGateway {
    FirebaseIdentity verifyIdToken(String idToken, boolean checkRevoked);

    String createSessionCookie(String idToken, Duration lifetime);

    FirebaseIdentity verifySessionCookie(String sessionCookie, boolean checkRevoked);

    void deleteUser(String firebaseUid);
}
