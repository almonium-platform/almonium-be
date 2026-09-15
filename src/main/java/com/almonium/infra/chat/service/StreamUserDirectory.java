package com.almonium.infra.chat.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.exception.StreamIntegrationException;
import io.getstream.chat.java.exceptions.StreamException;
import io.getstream.chat.java.models.DeleteStrategy;
import io.getstream.chat.java.models.Sort;
import io.getstream.chat.java.models.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** Reading and deleting Stream's user directory in the pages and batches Stream insists on. */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StreamUserDirectory {
    // Stream caps a query-users page at 100, and refuses paging past an offset of 1000.
    private static final int PAGE_SIZE = 100;
    private static final int MAX_OFFSET = 1000;

    /** Every user id Stream knows about, oldest first. */
    public List<String> listAllIds() {
        List<String> ids = new ArrayList<>();

        for (int offset = 0; offset <= MAX_OFFSET; offset += PAGE_SIZE) {
            List<User> page = listPage(offset);
            page.stream().map(User::getId).forEach(ids::add);

            if (page.size() < PAGE_SIZE) {
                return ids;
            }
        }

        log.warn("Stopped at Stream's paging limit of {} users; re-run once this batch is gone", MAX_OFFSET);
        return ids;
    }

    /** Hard-deletes the given users along with their messages and conversations. */
    public void deleteAll(List<String> userIds) {
        for (int from = 0; from < userIds.size(); from += PAGE_SIZE) {
            deleteBatch(userIds.subList(from, Math.min(from + PAGE_SIZE, userIds.size())));
        }
    }

    private List<User> listPage(int offset) {
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
                    String.format("Error while deleting %d Stream users: %s", userIds.size(), e.getMessage()), e);
        }
    }
}
