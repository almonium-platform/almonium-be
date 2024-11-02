package com.almonium.auth.common.exception;

public class RecentLoginRequiredException extends RuntimeException {
    public RecentLoginRequiredException(String message) {
        super(message);
    }
}
