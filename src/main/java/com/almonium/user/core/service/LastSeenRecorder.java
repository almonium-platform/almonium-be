package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.repository.ProfileRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 * Remembers that a user is here. Every authenticated request passes through this, so it writes at most once per user
 * per {@link #STAMP_INTERVAL} and keeps the rest in memory; the stamp exists to answer "was anyone around this week",
 * which does not need the minute. A failed write is logged and forgotten so the request it rode on is unaffected.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LastSeenRecorder {
    static final Duration STAMP_INTERVAL = Duration.ofMinutes(15);

    ProfileRepository profileRepository;
    Map<UUID, Instant> stampedAt = new ConcurrentHashMap<>();

    public void touch(UUID userId) {
        Instant now = Instant.now();
        Instant previous = stampedAt.get(userId);
        if (previous != null && previous.plus(STAMP_INTERVAL).isAfter(now)) {
            return;
        }
        stampedAt.put(userId, now);
        try {
            profileRepository.stampLastSeen(userId, now);
        } catch (DataAccessException e) {
            stampedAt.remove(userId);
            log.warn("Last seen could not be stamped for user {}", userId, e);
        }
    }
}
