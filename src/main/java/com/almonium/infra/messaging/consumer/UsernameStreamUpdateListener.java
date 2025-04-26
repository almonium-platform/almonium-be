package com.almonium.infra.messaging.consumer;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.core.events.UsernameUpdatedEvent;
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
public class UsernameStreamUpdateListener {
    StreamChatService streamChatService;

    UserRepository userRepository;

    @RabbitListener(queues = "${rabbitmq.queue.username-updated.name}")
    public void handleUsernameUpdate(UsernameUpdatedEvent event) {
        log.info("Received username changed event for userId: {}", event.userId());

        User user = userRepository.findById(event.userId()).orElse(null);

        if (user == null) {
            log.warn("User with ID {} not found, skipping Stream username update.", event.userId());
            return;
        }

        // Defensive check: Ensure username matches event (though unlikely to mismatch)
        if (!user.getUsername().equals(event.username())) {
            log.warn(
                    "Username mismatch for userId {}. DB has '{}', event has '{}'. Skipping Stream update.",
                    user.getId(),
                    user.getUsername(),
                    event.username());
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
