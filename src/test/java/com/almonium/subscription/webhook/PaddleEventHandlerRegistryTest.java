package com.almonium.subscription.webhook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class PaddleEventHandlerRegistryTest {
    @Test
    void indexesHandlersByEventType() {
        PaddleEventHandler handler = handler("subscription.created");

        PaddleEventHandlerRegistry registry = new PaddleEventHandlerRegistry(List.of(handler));

        assertThat(registry.find("subscription.created")).containsSame(handler);
        assertThat(registry.find("subscription.updated")).isEmpty();
    }

    @Test
    void rejectsDuplicateHandlers() {
        assertThatThrownBy(() -> new PaddleEventHandlerRegistry(
                        List.of(handler("subscription.created"), handler("subscription.created"))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate Paddle event handler");
    }

    private PaddleEventHandler handler(String eventType) {
        return new PaddleEventHandler() {
            @Override
            public String eventType() {
                return eventType;
            }

            @Override
            public void handle(PaddleEvent event) {}
        };
    }
}
