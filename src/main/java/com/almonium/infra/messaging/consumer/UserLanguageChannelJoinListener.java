package com.almonium.infra.messaging.consumer;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.core.events.UserLanguagesUpdatedEvent;
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
public class UserLanguageChannelJoinListener {
    StreamChatService streamChatService;
    UserRepository userRepository;

    @RabbitListener(queues = "${rabbitmq.queue.user-languages-updated.name}")
    public void handleUserLanguagesUpdated(UserLanguagesUpdatedEvent event) {
        log.info("Received user languages updated event for userId: {}", event.userId());

        User user = userRepository.findById(event.userId()).orElse(null);

        if (user == null) {
            log.warn("User with ID {} not found for language channel join.", event.userId());
            return;
        }

        if (event.targetLanguages() == null || event.targetLanguages().isEmpty()) {
            log.info("No target languages specified for userId: {}, skipping channel join.", event.userId());
            return;
        }

        try {
            // Call the external service
            streamChatService.joinLanguageSpecificChannelsIfAvailable(user, event.targetLanguages());
            log.info("Successfully processed language channel join for userId: {}", event.userId());

        } catch (StreamIntegrationException e) {
            log.error(
                    "Stream integration failed during language channel join for userId: {}. Error: {}",
                    event.userId(),
                    e.getMessage(),
                    e);
            // Re-throw specific exception to trigger retry/DLQ
            throw e;
        } catch (Exception e) {
            log.error(
                    "Unexpected error during language channel join for userId: {}. Error: {}",
                    event.userId(),
                    e.getMessage(),
                    e);
            // Wrap and re-throw to trigger retry/DLQ
            throw new EventProcessingException(
                    "Language channel join failed unexpectedly for user " + event.userId(), e);
        }
    }
}
