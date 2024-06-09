package com.almonium.auth.common.exception;

public class AuthMethodNotFoundException extends RuntimeException {
    public AuthMethodNotFoundException(String message) {
        super(message);
    }
}
