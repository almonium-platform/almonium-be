package com.almonium.subscription.webhook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;

class StripeEventHandlerRegistryTest {
    @Test
    void findsHandlerByExactStripeEventType() {
        StripeEventHandler handler = handlerFor("invoice.payment_failed");
        StripeEventHandlerRegistry registry = new StripeEventHandlerRegistry(List.of(handler));

        assertThat(registry.find("invoice.payment_failed")).contains(handler);
        assertThat(registry.find("invoice.paid")).isEmpty();
    }

    @Test
    void rejectsDuplicateEventHandlers() {
        StripeEventHandler first = handlerFor("customer.deleted");
        StripeEventHandler second = handlerFor("customer.deleted");

        assertThatThrownBy(() -> new StripeEventHandlerRegistry(List.of(first, second)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Duplicate Stripe event handler");
    }

    private StripeEventHandler handlerFor(String eventType) {
        StripeEventHandler handler = mock(StripeEventHandler.class);
        when(handler.eventType()).thenReturn(eventType);
        return handler;
    }
}
