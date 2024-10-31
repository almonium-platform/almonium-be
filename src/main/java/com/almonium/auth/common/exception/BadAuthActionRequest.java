package com.almonium.auth.common.exception;

public class BadAuthActionRequest extends RuntimeException {
    public BadAuthActionRequest(String message) {
        super(message);
    }
}
