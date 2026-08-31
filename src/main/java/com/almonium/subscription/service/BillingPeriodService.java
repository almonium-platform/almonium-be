package com.almonium.subscription.service;

import com.almonium.subscription.model.entity.PlanSubscription;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

/**
 * Resolves the allowance window that metered features are counted against.
 *
 * <p>Usage resets on the subscriber's billing-day anniversary, rather than on the
 * calendar first. This keeps monthly and annual plans predictable while still
 * granting an annual subscriber a fresh allowance each month.
 */
@Service
public class BillingPeriodService {

    public BillingPeriod currentPeriod(PlanSubscription subscription, Instant now) {
        Instant anchor = subscription.getStartDate() == null ? now : subscription.getStartDate();
        ZonedDateTime anchoredAt = anchor.atZone(ZoneOffset.UTC);
        ZonedDateTime nowAt = now.atZone(ZoneOffset.UTC);
        long elapsedMonths = Math.max(0, ChronoUnit.MONTHS.between(anchoredAt, nowAt));
        ZonedDateTime startsAt = anchoredAt.plusMonths(elapsedMonths);
        if (startsAt.isAfter(nowAt)) {
            startsAt = startsAt.minusMonths(1);
        }
        ZonedDateTime endsAt = startsAt.plusMonths(1);
        Instant subscriptionEnd = subscription.getEndDate();
        if (subscriptionEnd != null && subscriptionEnd.isBefore(endsAt.toInstant())) {
            endsAt = subscriptionEnd.atZone(ZoneOffset.UTC);
        }
        return new BillingPeriod(startsAt.toInstant(), endsAt.toInstant());
    }

    public record BillingPeriod(Instant startsAt, Instant endsAt) {}
}
