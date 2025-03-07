package com.almonium.infra.notification.cron;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.repository.NotificationRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NotificationCleanupTask {
    private static final int RETENTION_DAYS = 30;

    NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 3 * * ?") // Runs every day at 3 AM
    public void purgeOldReadNotifications() {
        Instant cutoff = Instant.now().minus(RETENTION_DAYS, ChronoUnit.DAYS);
        notificationRepository.deleteOldReadNotifications(cutoff);
    }
}
