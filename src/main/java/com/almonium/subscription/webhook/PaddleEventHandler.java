package com.almonium.subscription.webhook;

public interface PaddleEventHandler {
    String eventType();

    void handle(PaddleEvent event);
}
