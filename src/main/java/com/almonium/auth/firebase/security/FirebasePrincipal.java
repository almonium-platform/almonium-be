package com.almonium.auth.firebase.security;

import com.almonium.auth.common.model.PrincipalDetails;
import java.time.Instant;
import java.util.UUID;

public record FirebasePrincipal(
        String firebaseUid, UUID userId, String email, Instant authenticatedAt, String signInProvider)
        implements PrincipalDetails {

    @Override
    public UUID getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
