package com.almonium.infra.messaging.consumer;

import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.service.FriendshipEmailComposerService;
import com.almonium.infra.messaging.exception.EventProcessingException;
import com.almonium.user.relationship.event.FriendshipEmailRequestedEvent;
import com.almonium.user.relationship.model.enums.FriendshipEvent;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipEmailListener {
    private static final DateTimeFormatter EMAIL_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH).withZone(ZoneOffset.UTC);

    private final FriendshipEmailComposerService emailComposerService;

    @RabbitListener(queues = "${rabbitmq.queue.friendship-email-requested.name}")
    public void handleFriendshipEmailRequest(FriendshipEmailRequestedEvent event) {
        log.info(
                "Received friendship email request for email: {}, type: {}",
                event.recipientEmail(),
                event.friendshipEvent());

        try {
            EmailContext<FriendshipEvent> emailContext = new EmailContext<>(
                    event.friendshipEvent(),
                    Map.of(
                            FriendshipEmailComposerService.COUNTERPART_USERNAME,
                            event.counterpartUsername(),
                            FriendshipEmailComposerService.OCCURRED_AT,
                            formatOccurredAt(event.occurredAt())));

            emailComposerService.sendEmail(event.recipientUsername(), event.recipientEmail(), emailContext);

            log.info("Successfully processed friendship email request for {}", event.recipientEmail());

        } catch (Exception e) {
            log.error(
                    "Failed processing friendship email request for {}. Error: {}",
                    event.recipientEmail(),
                    e.getMessage(),
                    e);
            throw new EventProcessingException("Friendship email processing failed for " + event.recipientEmail(), e);
        }
    }

    private String formatOccurredAt(Instant occurredAt) {
        return occurredAt == null ? "" : EMAIL_DATE_FORMATTER.format(occurredAt);
    }
}
