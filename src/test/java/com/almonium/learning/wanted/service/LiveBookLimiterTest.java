package com.almonium.learning.wanted.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class LiveBookLimiterTest {
    LiveBookLimiter limiter = new LiveBookLimiter(2);
    UUID reader = UUID.randomUUID();
    Instant morning = Instant.parse("2026-11-27T08:00:00Z");

    @Test
    void aReaderGetsTheDaysShareAndThenWaitsForTheDayToPass() {
        assertThat(limiter.tryAcquire(reader, morning)).isTrue();
        assertThat(limiter.tryAcquire(reader, morning.plusSeconds(60))).isTrue();
        assertThat(limiter.tryAcquire(reader, morning.plusSeconds(120))).isFalse();
        // another reader has a share of their own
        assertThat(limiter.tryAcquire(UUID.randomUUID(), morning.plusSeconds(120)))
                .isTrue();

        assertThat(limiter.tryAcquire(reader, morning.plusSeconds(24 * 3600 + 1)))
                .isTrue();
    }

    @Test
    void aBookThatCouldNotBeStartedDoesNotCount() {
        assertThat(limiter.tryAcquire(reader, morning)).isTrue();
        assertThat(limiter.tryAcquire(reader, morning)).isTrue();
        limiter.release(reader);

        assertThat(limiter.tryAcquire(reader, morning)).isTrue();
    }
}
