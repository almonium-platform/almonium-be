package com.almonium.auth.firebase.exception;

import org.springframework.security.core.AuthenticationException;

public class FirebaseAuthenticationException extends AuthenticationException {
    public FirebaseAuthenticationException(String message) {
        super(message);
    }

    public FirebaseAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
