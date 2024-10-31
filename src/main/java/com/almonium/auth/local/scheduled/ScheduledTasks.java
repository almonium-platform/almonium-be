package com.almonium.auth.local.scheduled;

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
public class ScheduledTasks {
    private final VerificationTokenRepository tokenRepository;
    private final PrincipalRepository principalRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void purgeExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();

        // Delete principals attached to expired EMAIL_CHANGE tokens
        List<VerificationToken> expiredEmailChangeTokens =
                tokenRepository.findByTokenTypeAndExpiryDateBefore(TokenType.EMAIL_CHANGE, now);
        expiredEmailChangeTokens.stream().map(VerificationToken::getPrincipal).forEach(principalRepository::delete);

        // Delete other expired tokens
        tokenRepository.deleteByExpiryDateBefore(now);
    }
}
