package com.almonium.auth.oauth2.service;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AppleUserStore {
    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();
    private static final ThreadLocal<AppleUser> CONTEXT_AU = new ThreadLocal<>();

    public void setAppleUser(Map<String, Object> appleUser) {
        CONTEXT.set(appleUser);
    }

    public Map<String, Object> getAppleUser() {
        return CONTEXT.get();
    }

    public void removeAppleUser() {
        CONTEXT.remove();
    }
}
