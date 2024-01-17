package com.linguarium.util;

import com.linguarium.user.model.User;

import java.time.LocalDateTime;

public final class TestEntityGenerator {
    private TestEntityGenerator() {
    }

    public static User buildTestUser() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setPassword("password");
        user.setProvider("local");
        user.setRegistered(LocalDateTime.now());
        return user;
    }
}
