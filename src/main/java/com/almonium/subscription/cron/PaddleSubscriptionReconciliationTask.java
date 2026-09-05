package com.almonium.subscription.cron;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.subscription.service.CadenceChangeService;
import com.almonium.subscription.service.PaddleApiService;
import com.almonium.subscription.service.PlanSubscriptionService;
import java.time.Clock;
import java.time.Duration;
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

    /** The task runs daily, so a day's lead time guarantees one attempt before the renewal charge. */
    private static final Duration APPLY_AHEAD_OF_RENEWAL = Duration.ofDays(1);

    private final PlanSubscriptionRepository planSubscriptionRepository;
    private final PaddleApiService paddleApiService;
    private final PlanSubscriptionService planSubscriptionService;
    private final CadenceChangeService cadenceChangeService;
    private final Clock clock;

    @Scheduled(cron = "0 15 3 * * ?", zone = "UTC")
    public void reconcileTrackedSubscriptions() {
        List<PlanSubscription> subscriptions =
                planSubscriptionRepository.findAllByPaddleSubscriptionIdIsNotNullAndStatusIn(RECONCILABLE_STATUSES);
        for (PlanSubscription subscription : subscriptions) {
            applyScheduledCadenceChange(subscription);
            reconcile(subscription.getPaddleSubscriptionId());
        }
    }

    /**
     * Paddle cannot schedule a change of billing interval, so a pending cadence change is held locally and applied
     * here. It runs a day ahead of the date rather than on it, because the switch has to land before Paddle renews
     * the subscription for another full period at the cadence the member is leaving.
     */
    private void applyScheduledCadenceChange(PlanSubscription subscription) {
        Instant dueAt = subscription.getScheduledChangeAt();
        if (dueAt == null || clock.instant().isBefore(dueAt.minus(APPLY_AHEAD_OF_RENEWAL))) {
            return;
        }
        try {
            cadenceChangeService.applyDueChange(subscription);
        } catch (RuntimeException exception) {
            log.error(
                    "Failed to apply the scheduled cadence change for Paddle subscription {}; continuing",
                    subscription.getPaddleSubscriptionId(),
                    exception);
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
