package com.almonium.auth.firebase.service;

import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseAccountLookupService {
    private final FirebaseAuthGateway firebaseAuthGateway;

    public boolean accountExists(String email) {
        return firebaseAuthGateway.accountExistsByEmail(email.trim().toLowerCase(Locale.ROOT));
    }
}
