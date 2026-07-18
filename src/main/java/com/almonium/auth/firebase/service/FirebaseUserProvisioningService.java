package com.almonium.auth.firebase.service;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.factory.UserRegistrationService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseUserProvisioningService {
    private final UserRepository userRepository;
    private final UserRegistrationService userRegistrationService;

    public User resolveOrCreate(FirebaseIdentity identity) {
        String email = normalizeAndValidateEmail(identity);
        return userRepository
                .findByFirebaseUid(identity.uid())
                .map(user -> synchronizeIdentity(user, identity, email))
                .orElseGet(() -> createUser(identity, email));
    }

    private User createUser(FirebaseIdentity identity, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("An Almonium account already uses this email");
        }
        try {
            return userRegistrationService.createUserWithDefaultPlan(email, true, identity.uid());
        } catch (DataIntegrityViolationException exception) {
            return userRepository
                    .findByFirebaseUid(identity.uid())
                    .orElseThrow(() -> new ResourceConflictException(
                            "Firebase identity conflicts with an existing Almonium account"));
        }
    }

    private User synchronizeIdentity(User user, FirebaseIdentity identity, String email) {
        if (!user.getEmail().equalsIgnoreCase(email)) {
            userRepository
                    .findByEmail(email)
                    .filter(other -> !other.getId().equals(user.getId()))
                    .ifPresent(other -> {
                        throw new ResourceConflictException("An Almonium account already uses this email");
                    });
            user.setEmail(email);
        }
        user.setEmailVerified(identity.emailVerified());
        return userRepository.save(user);
    }

    private String normalizeAndValidateEmail(FirebaseIdentity identity) {
        if (!identity.emailVerified()) {
            throw new FirebaseAuthenticationException("Verify your email with Firebase before signing in");
        }
        if (identity.email() == null || identity.email().isBlank()) {
            throw new FirebaseAuthenticationException("Firebase account does not provide an email address");
        }
        return identity.email().trim().toLowerCase(Locale.ROOT);
    }
}
