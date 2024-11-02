package com.almonium.auth.local.cron;

import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class TokenCleanupTask {
    private final VerificationTokenRepository tokenRepository;
    private final PrincipalRepository principalRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void purgeExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();

        // Delete principals attached to expired EMAIL_CHANGE tokens
        List<VerificationToken> expiredEmailChangeTokens =
                tokenRepository.findByTokenTypeAndExpiresAtBefore(TokenType.EMAIL_CHANGE, now);

        principalRepository.deleteAll(expiredEmailChangeTokens.stream()
                .map(VerificationToken::getPrincipal)
                .toList());

        // Delete other expired tokens
        tokenRepository.deleteByExpiresAtBefore(now);
    }
}
