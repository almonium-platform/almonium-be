package com.almonium.subscription.cron;

import com.almonium.subscription.repository.PaddleEventLogRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaddleEventCleanupTask {
    private static final int DAYS_TO_RETAIN = 30;

    private final PaddleEventLogRepository eventLogRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanOldEvents() {
        eventLogRepository.deleteByReceivedAtBefore(Instant.now().minus(DAYS_TO_RETAIN, ChronoUnit.DAYS));
    }
}
