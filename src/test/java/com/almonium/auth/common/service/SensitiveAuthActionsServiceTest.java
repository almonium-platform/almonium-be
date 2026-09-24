package com.almonium.auth.common.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.auth.firebase.exception.FirebaseIdentityManagementException;
import com.almonium.auth.firebase.gateway.FirebaseAuthGateway;
import com.almonium.learning.book.service.UserBookImportService;
import com.almonium.subscription.exception.PlanSubscriptionException;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.service.FoundingMemberService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.events.UserDeletedEvent;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.List;
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
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class SensitiveAuthActionsServiceTest {
    @Mock
    PlanSubscriptionService planSubscriptionService;

    @Mock
    FoundingMemberService foundingMemberService;

    @Mock
    UserBookImportService userBookImportService;

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
                planSubscriptionService,
                foundingMemberService,
                userBookImportService,
                firebaseAuthGateway,
                userRepository,
                eventPublisher);
        user = User.builder()
                .id(UUID.randomUUID())
                .firebaseUid("firebase-uid")
                .email("reader@example.com")
                .username("reader")
                .build();
    }

    @Test
    void preflightFailureKeepsFirebaseAndLocalIdentitiesIntact() {
        when(planSubscriptionService.getPaidSubscriptionIdToCancel(user))
                .thenThrow(new PlanSubscriptionException("subscription lookup failed"));

        assertThatThrownBy(() -> service.deleteAccount(user)).isInstanceOf(PlanSubscriptionException.class);

        verifyNoInteractions(foundingMemberService, firebaseAuthGateway, eventPublisher, userRepository);
    }

    @Test
    void refusedLocalDeleteLeavesTheFirebaseIdentityInPlace() {
        when(planSubscriptionService.getPaidSubscriptionIdToCancel(user)).thenReturn(Optional.empty());
        org.mockito.Mockito.doThrow(new DataIntegrityViolationException("still referenced"))
                .when(userRepository)
                .flush();

        assertThatThrownBy(() -> service.deleteAccount(user)).isInstanceOf(DataIntegrityViolationException.class);

        verifyNoInteractions(firebaseAuthGateway, eventPublisher);
    }

    @Test
    void firebaseDeletionFailureDoesNotPublishCleanup() {
        when(planSubscriptionService.getPaidSubscriptionIdToCancel(user))
                .thenReturn(Optional.of("paddle-subscription"));
        org.mockito.Mockito.doThrow(
                        new FirebaseIdentityManagementException("Firebase unavailable", new IllegalStateException()))
                .when(firebaseAuthGateway)
                .deleteUser("firebase-uid");

        assertThatThrownBy(() -> service.deleteAccount(user)).isInstanceOf(FirebaseIdentityManagementException.class);

        verify(eventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void successfulDeletionRemovesLocalRowsBeforeFirebaseThenPublishesCleanup() {
        UUID importId = UUID.randomUUID();
        when(planSubscriptionService.getPaidSubscriptionIdToCancel(user))
                .thenReturn(Optional.of("paddle-subscription"));
        when(planSubscriptionService.getActivePlan(user))
                .thenReturn(Plan.builder().name("PREMIUM").build());
        when(userBookImportService.importIdsOwnedBy(user.getId())).thenReturn(List.of(importId));

        service.deleteAccount(user);

        ArgumentCaptor<UserDeletedEvent> eventCaptor = ArgumentCaptor.forClass(UserDeletedEvent.class);
        InOrder deletionOrder = inOrder(foundingMemberService, userRepository, firebaseAuthGateway, eventPublisher);
        deletionOrder.verify(foundingMemberService).releaseForDeletedAccount(user.getId());
        deletionOrder.verify(userRepository).delete(user);
        deletionOrder.verify(userRepository).flush();
        deletionOrder.verify(firebaseAuthGateway).deleteUser("firebase-uid");
        deletionOrder.verify(eventPublisher).publishEvent(eventCaptor.capture());

        UserDeletedEvent event = eventCaptor.getValue();
        org.assertj.core.api.Assertions.assertThat(event.userId()).isEqualTo(user.getId());
        org.assertj.core.api.Assertions.assertThat(event.paddleSubscriptionId()).contains("paddle-subscription");
        org.assertj.core.api.Assertions.assertThat(event.email()).isEqualTo("reader@example.com");
        org.assertj.core.api.Assertions.assertThat(event.username()).isEqualTo("reader");
        org.assertj.core.api.Assertions.assertThat(event.planName()).contains("PREMIUM");
        org.assertj.core.api.Assertions.assertThat(event.bookImportIds()).containsExactly(importId);
    }

    @Test
    void deletionWithoutPaidSubscriptionCarriesNoPlanName() {
        when(planSubscriptionService.getPaidSubscriptionIdToCancel(user)).thenReturn(Optional.empty());

        service.deleteAccount(user);

        ArgumentCaptor<UserDeletedEvent> eventCaptor = ArgumentCaptor.forClass(UserDeletedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        org.assertj.core.api.Assertions.assertThat(eventCaptor.getValue().planName())
                .isEmpty();
        verify(planSubscriptionService, never()).getActivePlan(user);
    }
}
