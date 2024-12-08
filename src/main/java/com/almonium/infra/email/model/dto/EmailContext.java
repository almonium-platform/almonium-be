package com.almonium.infra.email.model.dto;

import java.util.Map;

public record EmailContext<T>(T templateType, Map<String, String> attributes) {
    public EmailContext {
        if (templateType == null) {
            throw new IllegalArgumentException("Template type cannot be null");
        }
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes cannot be null");
        }
    }

    public String getValue(String key) {
        return attributes.get(key);
    }
}
