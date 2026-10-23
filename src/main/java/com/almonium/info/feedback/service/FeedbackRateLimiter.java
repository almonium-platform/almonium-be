package com.almonium.info.feedback.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/** The landing question is open to anyone, so each address gets three answers an hour. */
@Component
public class FeedbackRateLimiter {
    static final int MAX_SUBMISSIONS = 3;
    static final Duration WINDOW = Duration.ofHours(1);

    private final Cache<String, Deque<Instant>> submissions = CacheBuilder.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build();

    public boolean tryAcquire(String clientAddress) {
        return tryAcquire(clientAddress, Instant.now());
    }

    boolean tryAcquire(String clientAddress, Instant now) {
        Deque<Instant> recent = submissions.asMap().computeIfAbsent(clientAddress, ignored -> new ArrayDeque<>());
        synchronized (recent) {
            Instant cutoff = now.minus(WINDOW);
            while (!recent.isEmpty() && !recent.getFirst().isAfter(cutoff)) {
                recent.removeFirst();
            }
            if (recent.size() >= MAX_SUBMISSIONS) {
                return false;
            }
            recent.addLast(now);
            return true;
        }
    }
}
