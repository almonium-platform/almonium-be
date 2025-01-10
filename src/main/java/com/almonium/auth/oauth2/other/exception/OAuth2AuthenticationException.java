package com.almonium.auth.oauth2.other.exception;

import org.springframework.security.core.AuthenticationException;

// doesn't need handling - they are intercepted by the OAuth2AuthenticationFailureHandler
public class OAuth2AuthenticationException extends AuthenticationException {
    public OAuth2AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthenticationException(String msg) {
        super(msg);
    }
}
