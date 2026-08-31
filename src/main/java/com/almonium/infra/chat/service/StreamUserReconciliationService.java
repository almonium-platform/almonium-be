package com.almonium.infra.chat.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.config.properties.AppProperties;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.core.repository.UserRepository;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.DeleteStrategy;
import io.getstream.chat.java.models.Sort;
import io.getstream.chat.java.models.User;
import java.util.ArrayList;
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
    // Stream caps a query-users page at 100, and refuses paging past an offset of 1000.
    private static final int PAGE_SIZE = 100;
    private static final int MAX_OFFSET = 1000;

    UserRepository userRepository;
    AppProperties appProperties;

    /**
     * Stream users with no matching row in our database. The app's own system account, which owns
     * the broadcast channels and has no row by design, is never reported.
     */
    public List<String> findOrphans() {
        Set<String> known =
                userRepository.findAllIds().stream().map(UUID::toString).collect(Collectors.toSet());
        String systemUserId = appProperties.getName().toLowerCase();

        List<String> orphans = new ArrayList<>();
        for (int offset = 0; offset <= MAX_OFFSET; offset += PAGE_SIZE) {
            List<User> page = listUsers(offset);

            page.stream()
                    .map(User::getId)
                    .filter(id -> !known.contains(id) && !systemUserId.equals(id))
                    .forEach(orphans::add);

            if (page.size() < PAGE_SIZE) {
                return orphans;
            }
        }

        log.warn("Stopped at Stream's paging limit of {} users; re-run once this batch is gone", MAX_OFFSET);
        return orphans;
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

        for (int from = 0; from < orphans.size(); from += PAGE_SIZE) {
            deleteBatch(orphans.subList(from, Math.min(from + PAGE_SIZE, orphans.size())));
        }

        log.info("Deleted {} orphaned Stream users", orphans.size());
        return orphans;
    }

    private List<User> listUsers(int offset) {
        try {
            return User.list()
                    .filterCondition("role", "user")
                    .sort(Sort.builder()
                            .field("created_at")
                            .direction(Sort.Direction.ASC)
                            .build())
                    .limit(PAGE_SIZE)
                    .offset(offset)
                    .request()
                    .getUsers();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while listing Stream users at offset %d: %s", offset, e.getMessage()), e);
        }
    }

    private void deleteBatch(List<String> userIds) {
        try {
            User.deleteMany(userIds)
                    .deleteUserStrategy(DeleteStrategy.HARD)
                    .deleteMessagesStrategy(DeleteStrategy.HARD)
                    .deleteConversationsStrategy(DeleteStrategy.HARD)
                    .request();
        } catch (StreamException e) {
            throw new StreamIntegrationException(
                    String.format("Error while deleting %d orphaned Stream users: %s", userIds.size(), e.getMessage()),
                    e);
        }
    }
}
