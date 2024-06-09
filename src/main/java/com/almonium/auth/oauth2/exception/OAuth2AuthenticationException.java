package com.almonium.auth.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationException extends AuthenticationException {
    public OAuth2AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthenticationException(String msg) {
        super(msg);
    }
}
