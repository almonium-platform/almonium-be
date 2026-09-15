package com.almonium.auth.common.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.exception.FirebaseIdentityManagementException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.subscription.exception.PlanSubscriptionException;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.events.UserDeletedEvent;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class SensitiveAuthActionsServiceTest {
    @Mock
    PlanSubscriptionService planSubscriptionService;

    @Mock
    FirebaseAuthGateway firebaseAuthGateway;

    @Mock
    UserRepository userRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    SensitiveAuthActionsService service;
    User user;

    @BeforeEach
    void setUp() {
        service = new SensitiveAuthActionsService(
                planSubscriptionService, firebaseAuthGateway, userRepository, eventPublisher);
        user = User.builder().id(UUID.randomUUID()).firebaseUid("firebase-uid").build();
    }

    @Test
    void preflightFailureKeepsFirebaseAndLocalIdentitiesIntact() {
        when(planSubscriptionService.getPaidSubscriptionIdToCancel(user))
                .thenThrow(new PlanSubscriptionException("subscription lookup failed"));

        assertThatThrownBy(() -> service.deleteAccount(user)).isInstanceOf(PlanSubscriptionException.class);

        verifyNoInteractions(firebaseAuthGateway, eventPublisher, userRepository);
    }

    @Test
    void firebaseDeletionFailureDoesNotPublishCleanupOrDeleteLocalUser() {
        when(planSubscriptionService.getPaidSubscriptionIdToCancel(user))
                .thenReturn(Optional.of("paddle-subscription"));
        org.mockito.Mockito.doThrow(
                        new FirebaseIdentityManagementException("Firebase unavailable", new IllegalStateException()))
                .when(firebaseAuthGateway)
                .deleteUser("firebase-uid");

        assertThatThrownBy(() -> service.deleteAccount(user)).isInstanceOf(FirebaseIdentityManagementException.class);

        verify(eventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
        verify(userRepository, never()).delete(user);
    }

    @Test
    void successfulDeletionPublishesPreparedCleanupBeforeDeletingLocalUser() {
        when(planSubscriptionService.getPaidSubscriptionIdToCancel(user))
                .thenReturn(Optional.of("paddle-subscription"));

        service.deleteAccount(user);

        ArgumentCaptor<UserDeletedEvent> eventCaptor = ArgumentCaptor.forClass(UserDeletedEvent.class);
        InOrder deletionOrder = inOrder(firebaseAuthGateway, eventPublisher, userRepository);
        deletionOrder.verify(firebaseAuthGateway).deleteUser("firebase-uid");
        deletionOrder.verify(eventPublisher).publishEvent(eventCaptor.capture());
        deletionOrder.verify(userRepository).delete(user);

        UserDeletedEvent event = eventCaptor.getValue();
        org.assertj.core.api.Assertions.assertThat(event.userId()).isEqualTo(user.getId());
        org.assertj.core.api.Assertions.assertThat(event.paddleSubscriptionId()).contains("paddle-subscription");
    }
}
