package com.almonium.infra.messaging.consumer;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.core.events.UserProfileUpdatedEvent;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserProfileStreamUpdateListener {
    StreamChatService streamChatService;

    UserRepository userRepository;

    @RabbitListener(queues = "${rabbitmq.queue.user-profile-updated.name}")
    public void handleUserProfileUpdated(UserProfileUpdatedEvent event) {
        log.info("Received user profile updated event for userId: {}", event.userId());

        User user = userRepository.findById(event.userId()).orElse(null);
        if (user == null) {
            log.warn("User {} not found, skipping Stream profile update.", event.userId());
            return;
        }

        try {
            streamChatService.updateUser(user);
            log.info("Successfully updated username in Stream for userId: {}", event.userId());

        } catch (StreamIntegrationException e) {
            log.error(
                    "Stream integration failed during username update for userId: {}. Error: {}",
                    event.userId(),
                    e.getMessage(),
                    e);
            throw e;
        } catch (Exception e) {
            log.error(
                    "Unexpected error during Stream username update for userId: {}. Error: {}",
                    event.userId(),
                    e.getMessage(),
                    e);
            throw new EventProcessingException(
                    "Stream username update failed unexpectedly for user " + event.userId(), e);
        }
    }
}
