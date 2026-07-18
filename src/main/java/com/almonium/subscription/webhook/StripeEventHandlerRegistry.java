package com.almonium.subscription.webhook;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class StripeEventHandlerRegistry {
    private final Map<String, StripeEventHandler> handlers;

    public StripeEventHandlerRegistry(List<StripeEventHandler> handlers) {
        Map<String, StripeEventHandler> indexedHandlers = new LinkedHashMap<>();
        for (StripeEventHandler handler : handlers) {
            String eventType = handler.eventType();
            if (eventType == null || eventType.isBlank()) {
                throw new IllegalArgumentException("Stripe event type must not be blank");
            }
            StripeEventHandler duplicate = indexedHandlers.putIfAbsent(eventType, handler);
            if (duplicate != null) {
                throw new IllegalStateException("Duplicate Stripe event handler: " + eventType);
            }
        }
        this.handlers = Map.copyOf(indexedHandlers);
    }

    public Optional<StripeEventHandler> find(String eventType) {
        return Optional.ofNullable(handlers.get(eventType));
    }
}
