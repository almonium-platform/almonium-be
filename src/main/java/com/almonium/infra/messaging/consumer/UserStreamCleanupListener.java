package com.almonium.infra.messaging.consumer;

import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.core.events.UserDeletedEvent;
import com.almonium.user.core.exception.StreamIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStreamCleanupListener {
    private final StreamChatService streamChatService;

    @RabbitListener(queues = "${rabbitmq.queue.user-deleted-stream.name}")
    public void handleUserDeleted(UserDeletedEvent event) {
        log.info("Received user deleted event for userId: {}", event.userId());

        try {
            streamChatService.cleanUpUserData(event.userId());

            log.info("Successfully cleaned up Stream chat data for deleted userId: {}", event.userId());

        } catch (StreamIntegrationException e) {
            log.error(
                    "Stream integration failed during user cleanup for userId: {}. Error: {}",
                    event.userId(),
                    e.getMessage(),
                    e);
            throw e;
        } catch (Exception e) {
            log.error(
                    "Unexpected error during Stream user cleanup for userId: {}. Error: {}",
                    event.userId(),
                    e.getMessage(),
                    e);
            throw new EventProcessingException("Stream user cleanup failed unexpectedly for user " + event.userId(), e);
        }
    }
}
