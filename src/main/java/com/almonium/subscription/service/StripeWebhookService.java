package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.StripeEventLog;
import com.almonium.subscription.repository.StripeEventLogRepository;
import com.almonium.subscription.webhook.StripeEventHandlerRegistry;
import com.stripe.model.Event;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StripeWebhookService {
    StripeEventLogRepository stripeEventLogRepository;
    StripeEventHandlerRegistry handlerRegistry;

    private static final List<String> EXPECTED_EVENT_TYPES = List.of(
            "payment_intent.created",
            "customer.updated",
            "invoice.created",
            "invoice.finalized",
            "charge.succeeded",
            "payment_method.attached",
            "payment_intent.succeeded",
            "invoice.updated",
            "invoice.paid",
            "invoice.payment_succeeded",
            "entitlements.active_entitlement_summary.updated");

    @Transactional
    public void handleWebhook(Event event) {
        if (stripeEventLogRepository.existsById(event.getId())) {
            log.info("Event {} already processed, skipping", event.getId());
            return;
        }

        stripeEventLogRepository.save(
                new StripeEventLog(event.getId(), event.getType(), Instant.ofEpochSecond(event.getCreated())));
        log.info("Registered event: {}", event.getId());

        handlerRegistry
                .find(event.getType())
                .ifPresentOrElse(handler -> handler.handle(event), () -> handleOtherEvents(event.getType()));
    }

    private void handleOtherEvents(String type) {
        log.info(
                EXPECTED_EVENT_TYPES.contains(type)
                        ? "Expected but unhandled event type: {}"
                        : "Unexpected event type: {}",
                type);
    }
}
