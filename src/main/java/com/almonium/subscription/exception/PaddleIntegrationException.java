package com.almonium.subscription.exception;

public class PaddleIntegrationException extends RuntimeException {
    public PaddleIntegrationException(String message) {
        super(message);
    }

    public PaddleIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
