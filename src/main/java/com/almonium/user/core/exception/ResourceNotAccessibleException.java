package com.almonium.user.core.exception;

/**
 * Exception thrown when a resource either does not exist or is forbidden to access by the user.
 */
public class ResourceNotAccessibleException extends RuntimeException {
    public ResourceNotAccessibleException(String message) {
        super(message);
    }
}
