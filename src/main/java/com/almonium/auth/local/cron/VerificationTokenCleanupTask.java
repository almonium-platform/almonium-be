package com.almonium.auth.local.cron;

import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class VerificationTokenCleanupTask {
    private final VerificationTokenRepository verificationTokenRepository;
    private final PrincipalRepository principalRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void purgeExpiredVerificationTokens() {
        Instant now = Instant.now();

        // Delete principals attached to expired EMAIL_CHANGE tokens
        List<VerificationToken> expiredEmailChangeTokens =
                verificationTokenRepository.findByTokenTypeAndExpiresAtBefore(TokenType.EMAIL_CHANGE_VERIFICATION, now);

        principalRepository.deleteAll(expiredEmailChangeTokens.stream()
                .map(VerificationToken::getPrincipal)
                .toList());

        // Delete other expired tokens
        verificationTokenRepository.deleteByExpiresAtBefore(now);
    }
}
