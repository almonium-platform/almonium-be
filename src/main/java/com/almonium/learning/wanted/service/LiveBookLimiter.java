package com.almonium.learning.wanted.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * How many books one reader may have fetched while they wait, in a day. Each one costs the processor a core for up
 * to a minute and puts an unchecked text on everyone's shelf, so it is a handful, counted in memory like the other
 * limiters here: a restart forgets the day, which errs towards the reader.
 */
@Component
public class LiveBookLimiter {
    static final Duration WINDOW = Duration.ofDays(1);

    private final int dailyLimit;
    private final Cache<UUID, Deque<Instant>> started = CacheBuilder.newBuilder()
            .maximumSize(100_000)
            .expireAfterAccess(25, TimeUnit.HOURS)
            .build();

    public LiveBookLimiter(@Value("${app.books.live.daily-limit:5}") int dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public boolean tryAcquire(UUID readerId) {
        return tryAcquire(readerId, Instant.now());
    }

    boolean tryAcquire(UUID readerId, Instant now) {
        Deque<Instant> recent = started.asMap().computeIfAbsent(readerId, ignored -> new ArrayDeque<>());
        synchronized (recent) {
            Instant cutoff = now.minus(WINDOW);
            while (!recent.isEmpty() && !recent.getFirst().isAfter(cutoff)) {
                recent.removeFirst();
            }
            if (recent.size() >= dailyLimit) {
                return false;
            }
            recent.addLast(now);
            return true;
        }
    }

    /** Give back a place taken for a book that could not even be started. */
    public void release(UUID readerId) {
        Deque<Instant> recent = started.getIfPresent(readerId);
        if (recent != null) {
            synchronized (recent) {
                recent.pollLast();
            }
        }
    }
}
