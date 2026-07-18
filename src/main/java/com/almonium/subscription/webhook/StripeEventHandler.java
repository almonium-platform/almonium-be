package com.almonium.subscription.webhook;

import com.stripe.model.Event;

public interface StripeEventHandler {
    String eventType();

    void handle(Event event);
}
