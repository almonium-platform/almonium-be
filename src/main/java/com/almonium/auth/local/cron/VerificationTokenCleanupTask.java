package com.almonium.auth.local.cron;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.repository.PrincipalRepository;
import com.almonium.auth.local.model.entity.VerificationToken;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.auth.local.repository.VerificationTokenRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class VerificationTokenCleanupTask {
    VerificationTokenRepository verificationTokenRepository;
    PrincipalRepository principalRepository;

    @Scheduled(cron = "0 0 1 * * ?") // Runs every day at midnight
    @Transactional
    public void purgeExpiredVerificationTokens() {
        Instant now = Instant.now();

        // Delete principals attached to expired EMAIL_CHANGE tokens
        List<VerificationToken> expiredEmailChangeTokens =
                verificationTokenRepository.findByTokenTypeAndExpiresAtBefore(TokenType.EMAIL_CHANGE_VERIFICATION, now);

        principalRepository.deleteAllByIdInBatch(expiredEmailChangeTokens.stream()
                .map(VerificationToken::getPrincipal)
                .map(Principal::getId)
                .toList());

        // Delete other expired tokens
        verificationTokenRepository.deleteByExpiresAtBefore(now);
    }
}
