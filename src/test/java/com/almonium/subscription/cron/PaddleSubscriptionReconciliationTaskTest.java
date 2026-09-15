package com.almonium.subscription.cron;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.subscription.service.CadenceChangeService;
import com.almonium.subscription.service.PaddleApiService;
import com.almonium.subscription.service.PlanSubscriptionService;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaddleSubscriptionReconciliationTaskTest {
    private static final Instant OBSERVED_AT = Instant.parse("2026-08-18T03:15:00Z");

    @Mock
    PlanSubscriptionRepository planSubscriptionRepository;

    @Mock
    PaddleApiService paddleApiService;

    @Mock
    PlanSubscriptionService planSubscriptionService;

    @Mock
    CadenceChangeService cadenceChangeService;

    Clock clock = Clock.fixed(OBSERVED_AT, ZoneOffset.UTC);

    PaddleSubscriptionReconciliationTask task;

    @BeforeEach
    void setUp() {
        task = new PaddleSubscriptionReconciliationTask(
                planSubscriptionRepository, paddleApiService, planSubscriptionService, cadenceChangeService, clock);
    }

    @Test
    void reconcilesEveryTrackedNonterminalSubscriptionFromPaddle() {
        PlanSubscription subscription = PlanSubscription.builder()
                .paddleSubscriptionId("sub_01test")
                .status(PlanSubscription.Status.ACTIVE_TILL_CYCLE_END)
                .build();
        when(planSubscriptionRepository.findAllByPaddleSubscriptionIdIsNotNullAndStatusIn(List.of(
                        PlanSubscription.Status.ACTIVE,
                        PlanSubscription.Status.ACTIVE_TILL_CYCLE_END,
                        PlanSubscription.Status.PAUSED)))
                .thenReturn(List.of(subscription));
        PaddleApiService.SubscriptionSnapshot snapshot = new PaddleApiService.SubscriptionSnapshot(
                "sub_01test",
                "pri_monthly",
                "active",
                Optional.empty(),
                Optional.of(Instant.parse("2026-08-01T00:00:00Z")),
                Optional.of(Instant.parse("2026-09-01T00:00:00Z")));
        when(paddleApiService.getSubscription("sub_01test")).thenReturn(snapshot);

        task.reconcileTrackedSubscriptions();

        verify(planSubscriptionService)
                .reconcileSubscription(
                        snapshot.id(),
                        snapshot.priceId(),
                        snapshot.status(),
                        snapshot.scheduledChangeAction(),
                        snapshot.billingPeriodStartsAt(),
                        snapshot.billingPeriodEndsAt(),
                        OBSERVED_AT);
    }
}
