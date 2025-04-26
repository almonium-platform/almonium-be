package com.almonium.infra.messaging.consumer;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.core.events.UserRegisteredEvent;
import com.almonium.user.core.exception.StreamIntegrationException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserStreamSetupListener {
    StreamChatService streamChatService;
    UserRepository userRepository;
    TransactionTemplate transactionTemplate;

    @RabbitListener(queues = "${rabbitmq.queue.user-stream-setup.name}")
    public void handleUserRequiresStreamSetup(UserRegisteredEvent event) {
        log.info("Received user stream setup request for userId: {}", event.userId());

        User user = userRepository.findById(event.userId()).orElse(null);

        if (user == null) {
            log.warn("User with ID {} not found, skipping stream setup.", event.userId());
            return;
        }

        try {
            String token = streamChatService.setupNewUser(user);

            updateUserStreamToken(user.getId(), token);

            log.info("Successfully set up Stream chat for userId: {}", event.userId());

        } catch (StreamIntegrationException e) {
            log.error("Stream integration failed for userId: {}. Error: {}", event.userId(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(
                    "Unexpected error processing stream setup for userId: {}. Error: {}",
                    event.userId(),
                    e.getMessage(),
                    e);
            throw new EventProcessingException("Stream setup failed unexpectedly for user " + event.userId(), e);
        }
    }

    private void updateUserStreamToken(UUID userId, String token) {
        // Execute the database operation within a programmatic transaction
        transactionTemplate.executeWithoutResult(status -> userRepository
                .findById(userId)
                .ifPresentOrElse(
                        userToUpdate -> {
                            userToUpdate.setStreamChatToken(token);
                            userRepository.save(userToUpdate); // This save is now transactional
                            log.debug("Updated stream token for userId: {}", userId);
                        },
                        () -> log.warn("User with ID {} not found, skipping token update.", userId)));
    }
}
