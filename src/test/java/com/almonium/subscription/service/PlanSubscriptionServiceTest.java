package com.almonium.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.subscription.dto.response.PlanDto;
import com.almonium.subscription.event.SubscriptionStatusChangedEvent;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.PlanRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.PlanService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class PlanSubscriptionServiceTest {
    @Mock
    PaddleApiService paddleApiService;

    @Mock
    PaddlePriceCatalog paddlePriceCatalog;

    @Mock
    FoundingMemberService foundingMemberService;

    @Mock
    PlanService planService;

    @Mock
    PlanSubscriptionRepository subscriptionRepository;

    @Mock
    PlanRepository planRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    PlanSubscriptionService service;
    User user;
    Plan premium;

    @BeforeEach
    void setUp() {
        service = new PlanSubscriptionService(
                paddleApiService,
                paddlePriceCatalog,
                foundingMemberService,
                planService,
                subscriptionRepository,
                planRepository,
                userRepository,
                eventPublisher);
        user = User.builder()
                .id(UUID.randomUUID())
                .paddleCustomerId("ctm_01test")
                .build();
        Plan free = Plan.builder().id(1L).name("FREE").type(Plan.Type.LIFETIME).build();
        premium = Plan.builder().id(2L).name("PREMIUM").type(Plan.Type.MONTHLY).build();
        PlanSubscription freeSubscription = PlanSubscription.builder()
                .user(user)
                .plan(free)
                .status(PlanSubscription.Status.ACTIVE)
                .build();
        user.setEmail("member@example.com");
        user.setUsername("member");
        user.setPlanSubscriptions(new java.util.HashSet<>(List.of(freeSubscription)));
    }

    @Test
    void reservesFounderSlotBeforeCreatingCheckout() {
        stubCheckoutEligibility();
        when(foundingMemberService.reserveForCheckout(user)).thenReturn(Optional.of(3));
        when(paddleApiService.createPaymentTransaction(user, premium, Optional.of(3)))
                .thenReturn(new PaddleApiService.CheckoutTransaction("txn_01test", "https://checkout.test"));

        assertThat(service.initiatePlanSubscribing(user, 2L, true)).isEqualTo("https://checkout.test");

        verify(foundingMemberService).attachTransaction(3, user.getId(), "txn_01test");
    }

    @Test
    void doesNotSilentlyChargeRegularPriceWhenFounderOfferSoldOut() {
        stubCheckoutEligibility();
        when(foundingMemberService.reserveForCheckout(user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.initiatePlanSubscribing(user, 2L, true))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("no longer available");

        verifyNoInteractions(paddleApiService);
    }

    @Test
    void reactivatesOnlyWhenScheduledCancellationWasRemoved() {
        Instant cancellationTime = Instant.parse("2026-08-18T10:00:00Z");
        Instant reactivationTime = cancellationTime.plusSeconds(60);
        PlanSubscription subscription = paddleSubscription(PlanSubscription.Status.ACTIVE, null);
        stubPaddleSubscription(subscription);

        service.reconcileSubscription(
                "sub_01test",
                "pri_monthly",
                "active",
                Optional.of("cancel"),
                Optional.of(cancellationTime),
                Optional.of(cancellationTime.plusSeconds(3600)),
                cancellationTime);
        service.reconcileSubscription(
                "sub_01test",
                "pri_monthly",
                "active",
                Optional.empty(),
                Optional.of(cancellationTime),
                Optional.of(cancellationTime.plusSeconds(3600)),
                reactivationTime);

        assertThat(subscription.getStatus()).isEqualTo(PlanSubscription.Status.ACTIVE);
        assertThat(subscription.getLatestPaddleEventOccurredAt()).isEqualTo(reactivationTime);
        ArgumentCaptor<SubscriptionStatusChangedEvent> events =
                ArgumentCaptor.forClass(SubscriptionStatusChangedEvent.class);
        verify(eventPublisher, org.mockito.Mockito.times(2)).publishEvent(events.capture());
        assertThat(events.getAllValues())
                .extracting(SubscriptionStatusChangedEvent::subscriptionEvent)
                .containsExactly(PlanSubscription.Event.CANCELED, PlanSubscription.Event.REACTIVATED);
    }

    @Test
    void ignoresCancellationUpdateThatArrivesAfterNewerReactivation() {
        Instant reactivationTime = Instant.parse("2026-08-18T10:01:00Z");
        PlanSubscription subscription = paddleSubscription(PlanSubscription.Status.ACTIVE, reactivationTime);
        stubPaddleSubscriptionLookup(subscription);

        service.reconcileSubscription(
                "sub_01test",
                "pri_monthly",
                "active",
                Optional.of("cancel"),
                Optional.of(reactivationTime.minusSeconds(60)),
                Optional.of(reactivationTime.plusSeconds(3600)),
                reactivationTime.minusSeconds(60));

        assertThat(subscription.getStatus()).isEqualTo(PlanSubscription.Status.ACTIVE);
        assertThat(subscription.getLatestPaddleEventOccurredAt()).isEqualTo(reactivationTime);
        verify(eventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void ignoresTerminalCancellationThatIsOlderThanLatestUpdate() {
        Instant latest = Instant.parse("2026-08-18T10:01:00Z");
        PlanSubscription subscription = paddleSubscription(PlanSubscription.Status.ACTIVE, latest);
        stubPaddleSubscriptionLookup(subscription);

        service.cancelSubscription("sub_01test", latest.minusSeconds(1));

        assertThat(subscription.getStatus()).isEqualTo(PlanSubscription.Status.ACTIVE);
        verify(eventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void subscriptionCreatedDoesNotReactivateExistingSubscription() {
        Instant latest = Instant.parse("2026-08-18T10:00:00Z");
        PlanSubscription subscription = paddleSubscription(PlanSubscription.Status.ACTIVE_TILL_CYCLE_END, latest);
        stubPaddleSubscription(subscription);
        when(userRepository.findByPaddleCustomerId("ctm_01test")).thenReturn(Optional.of(user));

        service.syncSubscription(
                "ctm_01test",
                "pri_monthly",
                "sub_01test",
                "txn_01test",
                latest.plusSeconds(60),
                latest.plusSeconds(3600),
                Optional.empty(),
                latest.plusSeconds(60));

        assertThat(subscription.getStatus()).isEqualTo(PlanSubscription.Status.ACTIVE_TILL_CYCLE_END);
        verify(eventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void emitsRenewedForSuccessfulRecurringPayment() {
        Instant lifecycleTime = Instant.parse("2026-08-18T10:00:00Z");
        PlanSubscription subscription = paddleSubscription(PlanSubscription.Status.ACTIVE, lifecycleTime);
        stubPaddleSubscriptionLookup(subscription);

        service.notifyRenewed("sub_01test");

        ArgumentCaptor<SubscriptionStatusChangedEvent> event =
                ArgumentCaptor.forClass(SubscriptionStatusChangedEvent.class);
        verify(eventPublisher).publishEvent(event.capture());
        assertThat(event.getValue().subscriptionEvent()).isEqualTo(PlanSubscription.Event.RENEWED);
    }

    private void stubCheckoutEligibility() {
        when(planRepository.findById(2L)).thenReturn(Optional.of(premium));
        when(planService.getAvailableRecurringPremiumPlans())
                .thenReturn(List.of(new PlanDto(2L, "PREMIUM", Plan.Type.MONTHLY, "Premium", 12.0, 8.0, Map.of())));
        when(subscriptionRepository.findByUserAndStatusIn(
                        user, List.of(PlanSubscription.Status.ACTIVE, PlanSubscription.Status.ACTIVE_TILL_CYCLE_END)))
                .thenReturn(user.getPlanSubscriptions().stream().findFirst());
        when(planService.isPlanDefault(1L)).thenReturn(true);
    }

    private PlanSubscription paddleSubscription(PlanSubscription.Status status, Instant latestOccurredAt) {
        PlanSubscription subscription = PlanSubscription.builder()
                .id(UUID.randomUUID())
                .user(user)
                .plan(premium)
                .paddleSubscriptionId("sub_01test")
                .status(status)
                .startDate(Instant.parse("2026-08-01T00:00:00Z"))
                .endDate(Instant.parse("2026-09-01T00:00:00Z"))
                .latestPaddleEventOccurredAt(latestOccurredAt)
                .build();
        user.getPlanSubscriptions().add(subscription);
        return subscription;
    }

    private void stubPaddleSubscription(PlanSubscription subscription) {
        stubPaddleSubscriptionLookup(subscription);
        when(paddlePriceCatalog.planTypeFor("pri_monthly")).thenReturn(Plan.Type.MONTHLY);
        when(planRepository.findByNameAndType("PREMIUM", Plan.Type.MONTHLY)).thenReturn(Optional.of(premium));
    }

    private void stubPaddleSubscriptionLookup(PlanSubscription subscription) {
        when(subscriptionRepository.findForUpdateByPaddleSubscriptionId("sub_01test"))
                .thenReturn(Optional.of(subscription));
    }
}
