package com.almonium.infra.messaging.consumer;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.core.events.UserAddedTargetLanguageEvent;
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
public class TargetLanguageAddedListener {
    StreamChatService streamChatService;
    UserRepository userRepository;

    @RabbitListener(queues = "${rabbitmq.queue.user-target-language-added.name}")
    public void handleTargetLanguageAdded(UserAddedTargetLanguageEvent event) {
        log.info("Received target language added event for userId: {}, language: {}", event.userId(), event.language());

        User user = userRepository.findById(event.userId()).orElse(null);
        if (user == null) {
            log.warn(
                    "User {} not found, skipping Stream channel join for language {}",
                    event.userId(),
                    event.language());
            return;
        }

        try {
            streamChatService.joinLanguageSpecificChannelIfAvailable(user, event.language());
            log.info(
                    "Successfully joined Stream channel for language {} for user {}", event.language(), event.userId());
        } catch (StreamIntegrationException e) {
            log.error(
                    "Stream integration failed joining channel for lang {} / user {}. Error: {}",
                    event.language(),
                    event.userId(),
                    e.getMessage(),
                    e);
            throw e;
        } catch (Exception e) {
            log.error(
                    "Unexpected error joining channel for lang {} / user {}. Error: {}",
                    event.language(),
                    event.userId(),
                    e.getMessage(),
                    e);
            throw new EventProcessingException("Stream channel join failed unexpectedly", e);
        }
    }
}
