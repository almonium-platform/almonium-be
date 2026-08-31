package com.almonium.auth.firebase.service;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.factory.UserRegistrationService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseUserProvisioningService {
    // Providers that verify an address themselves, so their word is as good as a clicked link.
    private static final List<String> SELF_VERIFYING_PROVIDERS = List.of("google", "apple");

    private final UserRepository userRepository;
    private final UserRegistrationService userRegistrationService;
    private final FirebaseAuthGateway firebaseAuthGateway;

    public User resolveOrCreate(FirebaseIdentity identity) {
        String email = normalizeEmail(identity);
        return userRepository
                .findByFirebaseUid(identity.uid())
                .map(user -> synchronizeIdentity(user, identity, email))
                .orElseGet(() -> createUser(identity, email));
    }

    private User createUser(FirebaseIdentity identity, String email) {
        requireVerifiedEmail(identity, email);
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
        // An address our own row already vouches for needs no proof, and asking Firebase for one
        // would cost a call on every sign-in.
        boolean proofRequired = emailChanged || !user.isEmailVerified();
        if (proofRequired) {
            requireVerifiedEmail(identity, email);
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
        boolean verificationChanged = !user.isEmailVerified();
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

    /**
     * Firebase only raises the account's own verified flag when a federated provider is used to sign
     * in - linking one onto an existing password account leaves the flag where it was, forever, with
     * no path in the app to change it. So when the flag is down, ask who else vouches for the
     * address: a provider that verified it itself is proof, and the answer is written back so the
     * next token carries it.
     */
    private void requireVerifiedEmail(FirebaseIdentity identity, String email) {
        if (identity.emailVerified()) {
            return;
        }

        boolean vouchedFor = firebaseAuthGateway.getAuthProviders(identity.uid()).stream()
                .anyMatch(provider -> SELF_VERIFYING_PROVIDERS.contains(provider.provider())
                        && email.equalsIgnoreCase(provider.email()));

        if (!vouchedFor) {
            throw new FirebaseAuthenticationException("Verify your email with Firebase before signing in");
        }

        firebaseAuthGateway.markEmailVerified(identity.uid());
    }
}
