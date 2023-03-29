package com.linguatool.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistsAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = 5570981880007077317L;

    public UserAlreadyExistsAuthenticationException(final String msg) {
        super(msg);
    }
}
