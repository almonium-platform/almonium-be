package com.almonium.auth.oauth2.service;

import org.springframework.stereotype.Component;

@Component
public class AppleUserStore {
    private static final ThreadLocal<AppleUser> CONTEXT = new ThreadLocal<>();

    public void setAppleUser(AppleUser appleUser) {
        CONTEXT.set(appleUser);
    }

    public AppleUser getAppleUser() {
        return CONTEXT.get();
    }

    public void removeAppleUser() {
        CONTEXT.remove();
    }
}
