package com.almonium.subscription.service;

import com.almonium.subscription.model.entity.PaddleEventLog;
import com.almonium.subscription.repository.PaddleEventLogRepository;
import com.almonium.subscription.webhook.PaddleEvent;
import com.almonium.subscription.webhook.PaddleEventHandlerRegistry;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaddleWebhookService {
    private final PaddleEventLogRepository eventLogRepository;
    private final PaddleEventHandlerRegistry handlerRegistry;

    @Transactional
    public void handle(PaddleEvent event) {
        if (eventLogRepository.existsById(event.eventId())) {
            log.info("Paddle event {} was already processed", event.eventId());
            return;
        }
        eventLogRepository.save(
                new PaddleEventLog(event.eventId(), event.eventType(), event.occurredAt(), Instant.now()));
        handlerRegistry
                .find(event.eventType())
                .ifPresentOrElse(
                        handler -> handler.handle(event),
                        () -> log.info("Ignoring unhandled Paddle event type {}", event.eventType()));
    }
}
