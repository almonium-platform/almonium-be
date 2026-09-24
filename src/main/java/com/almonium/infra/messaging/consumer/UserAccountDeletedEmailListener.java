package com.almonium.infra.messaging.consumer;

import com.almonium.infra.email.model.dto.EmailContext;
import com.almonium.infra.email.model.enums.AuthEmailTemplateType;
import com.almonium.infra.email.service.AuthEmailComposerService;
import com.almonium.user.core.events.UserDeletedEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * The farewell mail. It is the one send with nothing to act on, and the only proof the user keeps that the deletion
 * happened; the address is in the event because the row it came from no longer exists. Never logged: the account is
 * gone and its address should not outlive it in our logs.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserAccountDeletedEmailListener {
    private final AuthEmailComposerService authEmailComposerService;

    @RabbitListener(queues = "${rabbitmq.queue.user-deleted-email.name}")
    public void handleUserDeleted(UserDeletedEvent event) {
        if (event.email() == null || event.email().isBlank()) {
            log.warn("No address on the deletion event for user {}, skipping the farewell email", event.userId());
            return;
        }

        Map<String, String> attributes = new HashMap<>();
        attributes.put(AuthEmailComposerService.USERNAME, event.username());
        attributes.put(AuthEmailComposerService.DELETED_AT, event.occurredAt().toString());
        event.planName().ifPresent(plan -> attributes.put(AuthEmailComposerService.PLAN_NAME, plan));

        authEmailComposerService.sendEmail(
                event.username(), event.email(), new EmailContext<>(AuthEmailTemplateType.ACCOUNT_DELETED, attributes));

        log.info("Sent the account-deleted email for user {}", event.userId());
    }
}
