package com.almonium.infra.chat.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.repository.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Reconciles the Stream user directory against our own. Stream keeps users we never told it to
 * forget: a dropped database, or a deletion that failed after the row was already gone, leaves an
 * account behind. Nothing upstream can notice that, so the fix is a pass that compares the two
 * directories.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StreamUserReconciliationService {
    UserRepository userRepository;
    StreamUserDirectory userDirectory;
    AppProperties appProperties;

    /**
     * Stream users with no matching row in our database. The app's own system account, which owns
     * the broadcast channels and has no row by design, is never reported.
     */
    public List<String> findOrphans() {
        Set<String> known =
                userRepository.findAllIds().stream().map(UUID::toString).collect(Collectors.toSet());
        String systemUserId = appProperties.getName().toLowerCase();

        return userDirectory.listAllIds().stream()
                .filter(id -> !known.contains(id) && !systemUserId.equals(id))
                .toList();
    }

    /**
     * Hard-deletes every orphan, along with its messages and conversations. Returns the ids removed,
     * so a caller can log them; call {@link #findOrphans()} first for a dry run.
     */
    public List<String> deleteOrphans() {
        List<String> orphans = findOrphans();
        if (orphans.isEmpty()) {
            log.info("Stream user directory is in sync, nothing to delete");
            return orphans;
        }

        userDirectory.deleteAll(orphans);
        log.info("Deleted {} orphaned Stream users", orphans.size());
        return orphans;
    }
}
