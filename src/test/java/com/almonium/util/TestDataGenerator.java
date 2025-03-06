package com.almonium.util;

import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.common.model.enums.AuthProviderType;
import com.almonium.auth.local.dto.request.LocalAuthRequest;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.infra.email.dto.EmailDto;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDataGenerator {

    public User buildTestUserWithId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setEmailVerified(true);
        user.setRegistered(Instant.now());
        user.setProfile(Profile.builder().user(user).build());
        user.setLearners(List.of(Learner.builder().user(user).build()));
        return user;
    }

    public Principal buildTestPrincipal(AuthProviderType providerType) {
        User user = buildTestUserWithId();
        return LocalPrincipal.builder()
                .user(user)
                .email(user.getEmail())
                .provider(providerType)
                .build();
    }

    public User buildTestUserWithId(UUID id) {
        User user = new User();
        user.setId(id);
        user.setUsername("john");
        user.setEmail("john@email.com");
        user.setRegistered(Instant.now());
        user.setProfile(Profile.builder().user(user).build());
        user.setLearners(List.of(Learner.builder().user(user).build()));
        return user;
    }

    public LocalAuthRequest createLocalAuthRequest() {
        return new LocalAuthRequest("dummy@example.com", "dummyPassword123");
    }

    public static LocalPrincipal buildTestLocalPrincipal() {
        return (LocalPrincipal) TestDataGenerator.buildTestPrincipal(AuthProviderType.LOCAL);
    }

    public static EmailDto createEmailDto() {
        return new EmailDto("recipient@mail.com", "Subject", "Body");
    }
}
