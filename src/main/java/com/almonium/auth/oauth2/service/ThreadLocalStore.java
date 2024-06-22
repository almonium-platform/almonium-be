package com.almonium.auth.oauth2.service;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ThreadLocalStore {
    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public void setAttributes(Map<String, Object> appleUser) {
        CONTEXT.set(appleUser);
    }

    public Map<String, Object> getAttributesAndClearContext() {
        Map<String, Object> result = CONTEXT.get();
        CONTEXT.remove();
        return result;
    }
}
