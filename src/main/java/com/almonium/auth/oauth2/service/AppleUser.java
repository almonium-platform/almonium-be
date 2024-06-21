package com.almonium.auth.oauth2.service;

import lombok.Data;

@Data
public class AppleUser {
    @Data
    public static class Name {
        private String firstName;
        private String lastName;
    }

    private Name name;
}
