package com.almonium.analyzer.client.ngrams.exception;

import com.almonium.analyzer.client.exception.ApiIntegrationException;

public class NgramsApiIntegrationException extends ApiIntegrationException {
    public NgramsApiIntegrationException(String message) {
        super(message);
    }
}
