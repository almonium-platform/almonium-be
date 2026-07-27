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
        String email = normalizeEmail(identity);
        return userRepository
                .findByFirebaseUid(identity.uid())
                .map(user -> synchronizeIdentity(user, identity, email))
                .orElseGet(() -> createUser(identity, email));
    }

    private User createUser(FirebaseIdentity identity, String email) {
        requireVerifiedEmail(identity);
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
        boolean emailChanged = !user.getEmail().equalsIgnoreCase(email);
        if (!identity.emailVerified() && (emailChanged || !user.isEmailVerified())) {
            throw new FirebaseAuthenticationException("Verify your email with Firebase before signing in");
        }
        if (emailChanged) {
            userRepository
                    .findByEmail(email)
                    .filter(other -> !other.getId().equals(user.getId()))
                    .ifPresent(other -> {
                        throw new ResourceConflictException("An Almonium account already uses this email");
                    });
            user.setEmail(email);
        }
        boolean verificationChanged = !user.isEmailVerified() && identity.emailVerified();
        if (verificationChanged) {
            user.setEmailVerified(true);
        }
        return emailChanged || verificationChanged ? userRepository.save(user) : user;
    }

    private String normalizeEmail(FirebaseIdentity identity) {
        if (identity.email() == null || identity.email().isBlank()) {
            throw new FirebaseAuthenticationException("Firebase account does not provide an email address");
        }
        return identity.email().trim().toLowerCase(Locale.ROOT);
    }

    private void requireVerifiedEmail(FirebaseIdentity identity) {
        if (!identity.emailVerified()) {
            throw new FirebaseAuthenticationException("Verify your email with Firebase before signing in");
        }
    }
}
