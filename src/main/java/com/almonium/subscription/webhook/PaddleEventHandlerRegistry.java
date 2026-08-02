package com.almonium.subscription.webhook;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class PaddleEventHandlerRegistry {
    private final Map<String, PaddleEventHandler> handlers;

    public PaddleEventHandlerRegistry(List<PaddleEventHandler> handlers) {
        Map<String, PaddleEventHandler> indexedHandlers = new LinkedHashMap<>();
        for (PaddleEventHandler handler : handlers) {
            String eventType = handler.eventType();
            if (eventType == null || eventType.isBlank()) {
                throw new IllegalArgumentException("Paddle event type must not be blank");
            }
            if (indexedHandlers.putIfAbsent(eventType, handler) != null) {
                throw new IllegalStateException("Duplicate Paddle event handler: " + eventType);
            }
        }
        this.handlers = Map.copyOf(indexedHandlers);
    }

    public Optional<PaddleEventHandler> find(String eventType) {
        return Optional.ofNullable(handlers.get(eventType));
    }
}
