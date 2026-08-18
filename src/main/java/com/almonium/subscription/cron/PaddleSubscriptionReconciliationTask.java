package com.almonium.subscription.cron;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.subscription.service.PaddleApiService;
import com.almonium.subscription.service.PlanSubscriptionService;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaddleSubscriptionReconciliationTask {
    private static final List<PlanSubscription.Status> RECONCILABLE_STATUSES = List.of(
            PlanSubscription.Status.ACTIVE,
            PlanSubscription.Status.ACTIVE_TILL_CYCLE_END,
            PlanSubscription.Status.PAUSED);

    private final PlanSubscriptionRepository planSubscriptionRepository;
    private final PaddleApiService paddleApiService;
    private final PlanSubscriptionService planSubscriptionService;
    private final Clock clock;

    @Scheduled(cron = "0 15 3 * * ?", zone = "UTC")
    public void reconcileTrackedSubscriptions() {
        List<PlanSubscription> subscriptions =
                planSubscriptionRepository.findAllByPaddleSubscriptionIdIsNotNullAndStatusIn(RECONCILABLE_STATUSES);
        for (PlanSubscription subscription : subscriptions) {
            reconcile(subscription.getPaddleSubscriptionId());
        }
    }

    private void reconcile(String subscriptionId) {
        try {
            Instant requestedAt = clock.instant();
            PaddleApiService.SubscriptionSnapshot snapshot = paddleApiService.getSubscription(subscriptionId);
            planSubscriptionService.reconcileSubscription(
                    snapshot.id(),
                    snapshot.priceId(),
                    snapshot.status(),
                    snapshot.scheduledChangeAction(),
                    snapshot.billingPeriodStartsAt(),
                    snapshot.billingPeriodEndsAt(),
                    requestedAt);
        } catch (RuntimeException exception) {
            log.error("Failed to reconcile Paddle subscription {}; continuing", subscriptionId, exception);
        }
    }
}
