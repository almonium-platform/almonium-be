package com.almonium.subscription.cron;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.repository.StripeEventLogRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StripeEventCleanupTask {
    private static final int DAYS_TO_RETAIN = 10;

    StripeEventLogRepository stripeEventLogRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void cleanOldEvents() {
        Instant retentionPeriod = Instant.now().minus(DAYS_TO_RETAIN, ChronoUnit.DAYS);
        stripeEventLogRepository.deleteByCreatedAtBefore(retentionPeriod);
    }
}
