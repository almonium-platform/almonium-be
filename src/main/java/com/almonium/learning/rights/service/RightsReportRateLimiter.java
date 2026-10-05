package com.almonium.learning.rights.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

/** The report form is open to guests, so each address gets five reports an hour. */
@Component
public class RightsReportRateLimiter {
    static final int MAX_REPORTS = 5;
    static final Duration WINDOW = Duration.ofHours(1);

    private final Cache<String, Deque<Instant>> reports = CacheBuilder.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build();

    public boolean tryAcquire(String clientAddress) {
        return tryAcquire(clientAddress, Instant.now());
    }

    boolean tryAcquire(String clientAddress, Instant now) {
        Deque<Instant> recent = reports.asMap().computeIfAbsent(clientAddress, ignored -> new ArrayDeque<>());
        synchronized (recent) {
            Instant cutoff = now.minus(WINDOW);
            while (!recent.isEmpty() && !recent.getFirst().isAfter(cutoff)) {
                recent.removeFirst();
            }
            if (recent.size() >= MAX_REPORTS) {
                return false;
            }
            recent.addLast(now);
            return true;
        }
    }
}
