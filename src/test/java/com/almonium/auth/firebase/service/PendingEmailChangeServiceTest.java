package com.almonium.auth.firebase.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.auth.firebase.model.PendingEmailChange;
import com.almonium.auth.firebase.repository.PendingEmailChangeRepository;
import com.almonium.config.properties.AppProperties;
import com.almonium.infra.email.service.AuthEmailComposerService;
import com.almonium.user.core.exception.BadUserRequestActionException;
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

@ExtendWith(MockitoExtension.class)
class PendingEmailChangeServiceTest {
    @Mock
    PendingEmailChangeRepository pendingEmailChangeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    FirebaseAuthGateway firebaseAuthGateway;

    @Mock
    AuthEmailComposerService emailComposerService;

    PendingEmailChangeService service;

    @BeforeEach
    void setUp() {
        AppProperties properties = new AppProperties();
        properties.setWebDomain("https://almonium.com");
        service = new PendingEmailChangeService(
                pendingEmailChangeRepository, userRepository, firebaseAuthGateway, emailComposerService, properties);
    }

    @Test
    void confirmsOneTimeEmailChangeAndRevokesFirebaseCredentials() {
        UUID userId = UUID.randomUUID();
        PendingEmailChange pending = new PendingEmailChange(
                userId, "new@example.com", "hash", Instant.now().plusSeconds(60));
        User user = User.builder()
                .id(userId)
                .firebaseUid("firebase-uid")
                .email("old@example.com")
                .build();
        when(pendingEmailChangeRepository.findByTokenHash(any())).thenReturn(Optional.of(pending));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        service.confirm("one-time-token");

        verify(firebaseAuthGateway).updateEmail("firebase-uid", "new@example.com");
        verify(userRepository).save(user);
        verify(pendingEmailChangeRepository).delete(pending);
        org.assertj.core.api.Assertions.assertThat(user.getEmail()).isEqualTo("new@example.com");
        org.assertj.core.api.Assertions.assertThat(user.isEmailVerified()).isTrue();
    }

    @Test
    void rejectsExpiredEmailChangeWithoutMutatingFirebase() {
        PendingEmailChange pending = new PendingEmailChange(
                UUID.randomUUID(), "new@example.com", "hash", Instant.now().minusSeconds(1));
        when(pendingEmailChangeRepository.findByTokenHash(any())).thenReturn(Optional.of(pending));

        assertThatThrownBy(() -> service.confirm("expired-token"))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("expired");

        verify(firebaseAuthGateway, never()).updateEmail(any(), any());
        verify(pendingEmailChangeRepository).delete(pending);
    }
}
