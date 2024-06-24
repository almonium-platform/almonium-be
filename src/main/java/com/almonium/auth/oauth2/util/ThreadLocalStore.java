package com.almonium.auth.oauth2.util;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ThreadLocalStore {
    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public void addAttributes(Map<String, Object> newAttributes) {
        Map<String, Object> existingAttributes = CONTEXT.get();
        if (existingAttributes == null) {
            CONTEXT.set(newAttributes);
        } else {
            existingAttributes.putAll(newAttributes);
        }
    }

    public Map<String, Object> getAttributesAndClearContext() {
        Map<String, Object> result = CONTEXT.get();
        CONTEXT.remove();
        return result;
    }

    public void clearContext() {
        CONTEXT.remove();
    }
}
