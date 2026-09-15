package com.almonium.auth.firebase.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class AccountLookupRateLimiter {
    static final int MAX_ATTEMPTS = 10;
    static final Duration WINDOW = Duration.ofMinutes(1);

    private final Cache<String, AttemptWindow> attempts = CacheBuilder.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    public boolean tryAcquire(String clientAddress) {
        return tryAcquire(clientAddress, Instant.now());
    }

    boolean tryAcquire(String clientAddress, Instant now) {
        AttemptWindow window = attempts.asMap().computeIfAbsent(clientAddress, ignored -> new AttemptWindow());
        return window.tryAcquire(now);
    }

    private static final class AttemptWindow {
        private final Deque<Instant> attempts = new ArrayDeque<>();

        private synchronized boolean tryAcquire(Instant now) {
            Instant cutoff = now.minus(WINDOW);
            while (!attempts.isEmpty() && !attempts.getFirst().isAfter(cutoff)) {
                attempts.removeFirst();
            }
            if (attempts.size() >= MAX_ATTEMPTS) {
                return false;
            }
            attempts.addLast(now);
            return true;
        }
    }
}
