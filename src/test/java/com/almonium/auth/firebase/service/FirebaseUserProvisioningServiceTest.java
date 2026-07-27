package com.almonium.auth.firebase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.exception.FirebaseAuthenticationException;
import com.almonium.auth.firebase.model.FirebaseIdentity;
import com.almonium.user.core.exception.ResourceConflictException;
import com.almonium.user.core.factory.UserRegistrationService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class FirebaseUserProvisioningServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserRegistrationService registrationService;

    FirebaseUserProvisioningService service;

    @BeforeEach
    void setUp() {
        service = new FirebaseUserProvisioningService(userRepository, registrationService);
    }

    @Test
    void createsNewUserByUidWithoutEmailMerging() {
        FirebaseIdentity identity = identity("New@Example.com", true);
        User created = User.builder()
                .firebaseUid(identity.uid())
                .email("new@example.com")
                .build();
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.empty());
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(registrationService.createUserWithDefaultPlan("new@example.com", true, identity.uid()))
                .thenReturn(created);

        assertThat(service.resolveOrCreate(identity)).isSameAs(created);
    }

    @Test
    void rejectsMatchingEmailBelongingToUnmappedUser() {
        FirebaseIdentity identity = identity("existing@example.com", true);
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.empty());
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.resolveOrCreate(identity))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("already uses");

        verifyNoInteractions(registrationService);
    }

    @Test
    void synchronizesEmailOnlyForSameFirebaseUid() {
        FirebaseIdentity identity = identity("changed@example.com", true);
        User existing = User.builder()
                .id(UUID.randomUUID())
                .firebaseUid(identity.uid())
                .email("old@example.com")
                .build();
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.of(existing));
        when(userRepository.findByEmail("changed@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(existing)).thenReturn(existing);

        User result = service.resolveOrCreate(identity);

        assertThat(result.getEmail()).isEqualTo("changed@example.com");
        verify(userRepository).save(existing);
    }

    @Test
    void acceptsPreviouslyVerifiedUnchangedEmailWhenFirebaseFlagTurnsFalse() {
        FirebaseIdentity identity = identity("existing@example.com", false);
        User existing = User.builder()
                .id(UUID.randomUUID())
                .firebaseUid(identity.uid())
                .email(identity.email())
                .emailVerified(true)
                .build();
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.of(existing));

        assertThat(service.resolveOrCreate(identity)).isSameAs(existing);
        assertThat(existing.isEmailVerified()).isTrue();
    }

    @Test
    void rejectsUnverifiedChangedEmailForExistingUser() {
        FirebaseIdentity identity = identity("changed@example.com", false);
        User existing = User.builder()
                .id(UUID.randomUUID())
                .firebaseUid(identity.uid())
                .email("verified@example.com")
                .emailVerified(true)
                .build();
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.resolveOrCreate(identity))
                .isInstanceOf(FirebaseAuthenticationException.class)
                .hasMessageContaining("Verify");
    }

    @Test
    void rejectsUnverifiedEmailForExistingUnverifiedUser() {
        FirebaseIdentity identity = identity("existing@example.com", false);
        User existing = User.builder()
                .id(UUID.randomUUID())
                .firebaseUid(identity.uid())
                .email(identity.email())
                .emailVerified(false)
                .build();
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.resolveOrCreate(identity))
                .isInstanceOf(FirebaseAuthenticationException.class)
                .hasMessageContaining("Verify");
    }

    @Test
    void concurrentProvisioningOfSameUidReturnsWinningUser() {
        FirebaseIdentity identity = identity("new@example.com", true);
        User winningUser = User.builder()
                .id(UUID.randomUUID())
                .firebaseUid(identity.uid())
                .email(identity.email())
                .build();
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.empty(), Optional.of(winningUser));
        when(userRepository.existsByEmail(identity.email())).thenReturn(false);
        when(registrationService.createUserWithDefaultPlan(identity.email(), true, identity.uid()))
                .thenThrow(new DataIntegrityViolationException("duplicate uid"));

        assertThat(service.resolveOrCreate(identity)).isSameAs(winningUser);
    }

    @Test
    void concurrentProvisioningOfConflictingIdentityReturnsConflict() {
        FirebaseIdentity identity = identity("new@example.com", true);
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.empty());
        when(userRepository.existsByEmail(identity.email())).thenReturn(false);
        when(registrationService.createUserWithDefaultPlan(identity.email(), true, identity.uid()))
                .thenThrow(new DataIntegrityViolationException("different unique constraint"));

        assertThatThrownBy(() -> service.resolveOrCreate(identity))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("conflicts");
    }

    @Test
    void rejectsUnverifiedEmailForNewUser() {
        FirebaseIdentity identity = identity("new@example.com", false);
        when(userRepository.findByFirebaseUid(identity.uid())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.resolveOrCreate(identity))
                .isInstanceOf(FirebaseAuthenticationException.class)
                .hasMessageContaining("Verify");

        verifyNoInteractions(registrationService);
    }

    private FirebaseIdentity identity(String email, boolean verified) {
        return new FirebaseIdentity("firebase-uid", email, verified, Instant.now(), "password");
    }
}
