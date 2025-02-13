package com.almonium.user.core.exception;

public class StreamIntegrationException extends RuntimeException {
    public StreamIntegrationException(String message) {
        super(message);
    }

    public StreamIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
