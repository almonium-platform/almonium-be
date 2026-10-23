package com.almonium.info.feedback.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class FeedbackRateLimiterTest {
    FeedbackRateLimiter limiter = new FeedbackRateLimiter();
    Instant now = Instant.parse("2026-10-02T18:00:00Z");

    @Test
    void anAddressGetsThreeAnswersAnHour() {
        assertThat(limiter.tryAcquire("203.0.113.7", now)).isTrue();
        assertThat(limiter.tryAcquire("203.0.113.7", now.plusSeconds(60))).isTrue();
        assertThat(limiter.tryAcquire("203.0.113.7", now.plusSeconds(120))).isTrue();
        assertThat(limiter.tryAcquire("203.0.113.7", now.plusSeconds(180))).isFalse();
        assertThat(limiter.tryAcquire("198.51.100.2", now.plusSeconds(180))).isTrue();
        assertThat(limiter.tryAcquire(
                        "203.0.113.7", now.plus(FeedbackRateLimiter.WINDOW).plusSeconds(1)))
                .isTrue();
    }
}
