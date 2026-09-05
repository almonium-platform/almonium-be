package com.almonium.util;

import com.almonium.infra.email.dto.EmailDto;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public final class TestDataGenerator {
    private TestDataGenerator() {}

    public static User buildTestUserWithId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setEmailVerified(true);
        user.setRegistered(Instant.now());
        user.setProfile(Profile.builder().user(user).build());
        user.setLearners(Set.of(Learner.builder().user(user).build()));
        return user;
    }

    public static User buildTestUserWithId(UUID id) {
        User user = new User();
        user.setId(id);
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setRegistered(Instant.now());
        user.setProfile(Profile.builder().user(user).build());
        user.setLearners(Set.of(Learner.builder().user(user).build()));
        return user;
    }

    public static EmailDto createEmailDto() {
        return new EmailDto("recipient@mail.com", "Subject", "Body");
    }
}
