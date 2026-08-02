package com.almonium.subscription.exception;

public class InvalidPaddleWebhookException extends RuntimeException {
    public InvalidPaddleWebhookException(String message) {
        super(message);
    }

    public InvalidPaddleWebhookException(String message, Throwable cause) {
        super(message, cause);
    }
}
