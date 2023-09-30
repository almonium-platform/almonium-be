package com.linguarium.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistsAuthenticationException extends AuthenticationException {

    public UserAlreadyExistsAuthenticationException(final String msg) {
        super(msg);
    }
}
