package com.almonium.infra.messaging.consumer;

import com.almonium.learning.book.service.BookProcessorClient;
import com.almonium.user.core.events.UserDeletedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Deletes a deleted account's imported books from the processor, which keeps the only copy of each upload. The import
 * rows already went with the account, so the ids come on the event. A retry is safe: the processor answering that an
 * import is already gone counts as done.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserBookImportCleanupListener {
    private final BookProcessorClient processorClient;

    @RabbitListener(queues = "${rabbitmq.queue.user-deleted-book-imports.name}")
    public void handleUserDeleted(UserDeletedEvent event) {
        if (event.bookImportIds().isEmpty()) {
            return;
        }
        for (UUID importId : event.bookImportIds()) {
            processorClient.deletePrivateImport(importId, event.userId());
        }
        log.info(
                "Deleted {} imported books from the processor for user {}",
                event.bookImportIds().size(),
                event.userId());
    }
}
