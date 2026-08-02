package com.almonium.auth.firebase.model;

import java.time.Instant;

public record FirebaseIdentity(
        String uid,
        String email,
        boolean emailVerified,
        Instant authenticatedAt,
        String signInProvider,
        boolean admin) {

    public FirebaseIdentity(
            String uid, String email, boolean emailVerified, Instant authenticatedAt, String signInProvider) {
        this(uid, email, emailVerified, authenticatedAt, signInProvider, false);
    }
}
