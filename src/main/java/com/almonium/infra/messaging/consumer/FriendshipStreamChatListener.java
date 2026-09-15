package com.almonium.infra.messaging.consumer;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.relationship.event.FriendshipAcceptedEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipStreamChatListener {
    StreamChatService streamChatService;

    @RabbitListener(queues = "${rabbitmq.queue.friendship-accepted.name}")
    public void handleFriendshipAccepted(FriendshipAcceptedEvent event) {
        log.info("Creating the private chat for friendship: {}", event.relationshipId());

        try {
            streamChatService.createPrivateChat(event.relationshipId(), event.accepterId(), event.counterpartId());

            log.info("Successfully created the private chat for friendship: {}", event.relationshipId());

        } catch (StreamIntegrationException e) {
            log.error(
                    "Stream integration failed for friendship: {}. Error: {}",
                    event.relationshipId(),
                    e.getMessage(),
                    e);
            throw e;
        } catch (Exception e) {
            log.error(
                    "Unexpected error creating the private chat for friendship: {}. Error: {}",
                    event.relationshipId(),
                    e.getMessage(),
                    e);
            throw new EventProcessingException(
                    "Private chat creation failed unexpectedly for friendship " + event.relationshipId(), e);
        }
    }
}
