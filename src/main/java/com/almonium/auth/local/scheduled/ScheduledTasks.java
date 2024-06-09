package com.almonium.auth.local.scheduled;

import com.almonium.auth.local.repository.VerificationTokenRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class ScheduledTasks {
    private final VerificationTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void purgeExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}
