package com.almonium.user.core.exception;

public class NoPrincipalFoundException extends IllegalStateException {
    public NoPrincipalFoundException(String message) {
        super(message);
    }
}
