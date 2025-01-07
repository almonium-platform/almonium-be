package com.almonium.user.core.exception;

public class FirebaseIntegrationException extends RuntimeException {
    public FirebaseIntegrationException(String message) {
        super(message);
    }

    public FirebaseIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
