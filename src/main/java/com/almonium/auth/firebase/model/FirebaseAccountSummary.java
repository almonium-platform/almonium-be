package com.almonium.auth.firebase.model;

import java.util.List;

/** What Firebase knows about an account, which is not always what our database believes. */
public record FirebaseAccountSummary(
        String uid, String email, boolean emailVerified, List<FirebaseAuthProvider> providers) {}
