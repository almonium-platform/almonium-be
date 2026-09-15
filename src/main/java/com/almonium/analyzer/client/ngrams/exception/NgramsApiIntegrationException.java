package com.almonium.analyzer.client.ngrams.exception;

import com.almonium.analyzer.client.exception.ApiIntegrationException;

public class NgramsApiIntegrationException extends ApiIntegrationException {
    private final boolean retryable;

    public NgramsApiIntegrationException(String message) {
        this(message, false, null);
    }

    public NgramsApiIntegrationException(String message, boolean retryable, Throwable cause) {
        super(message, cause);
        this.retryable = retryable;
    }

    public boolean isRetryable() {
        return retryable;
    }
}
