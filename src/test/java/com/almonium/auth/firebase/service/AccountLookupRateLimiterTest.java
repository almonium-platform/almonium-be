package com.almonium.auth.firebase.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class AccountLookupRateLimiterTest {
    private final AccountLookupRateLimiter limiter = new AccountLookupRateLimiter();

    @Test
    void limitsEachClientWithinSlidingWindow() {
        Instant now = Instant.parse("2026-08-29T12:00:00Z");

        for (int attempt = 0; attempt < AccountLookupRateLimiter.MAX_ATTEMPTS; attempt++) {
            assertThat(limiter.tryAcquire("192.0.2.1", now)).isTrue();
        }

        assertThat(limiter.tryAcquire("192.0.2.1", now)).isFalse();
        assertThat(limiter.tryAcquire("192.0.2.2", now)).isTrue();
        assertThat(limiter.tryAcquire("192.0.2.1", now.plus(AccountLookupRateLimiter.WINDOW)))
                .isTrue();
    }
}
