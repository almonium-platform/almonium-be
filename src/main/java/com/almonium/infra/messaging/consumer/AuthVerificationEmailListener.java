package com.almonium.infra.messaging.consumer;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.events.VerificationEmailRequestedEvent;
import com.almonium.auth.local.model.enums.TokenType;
import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.AuthTokenEmailComposerService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AuthVerificationEmailListener {
    AuthTokenEmailComposerService emailComposerService;
    UserRepository userRepository;

    @RabbitListener(queues = "${rabbitmq.queue.auth-verification-email.name}")
    public void handleVerificationEmailRequest(VerificationEmailRequestedEvent event) {
        log.info(
                "Received verification email request for email: {}, type: {}",
                event.recipientEmail(),
                event.tokenType());

        User user = userRepository.findById(event.userId()).orElse(null);
        if (user == null) {
            log.warn(
                    "User {} not found for verification email to {}, token type {}. Skipping.",
                    event.userId(),
                    event.recipientEmail(),
                    event.tokenType());
            return;
        }

        try {
            EmailContext<TokenType> emailContext = new EmailContext<>(
                    event.tokenType(), Map.of(AuthTokenEmailComposerService.TOKEN_ATTRIBUTE, event.token()));

            emailComposerService.sendEmail(user.getUsername(), event.recipientEmail(), emailContext);

            log.info("Successfully processed verification email request for {}", event.recipientEmail());
        } catch (Exception e) {
            log.error(
                    "Failed processing verification email request for {}. Error: {}",
                    event.recipientEmail(),
                    e.getMessage(),
                    e);
            throw new EventProcessingException("Verification email processing failed for " + event.recipientEmail(), e);
        }
    }
}
