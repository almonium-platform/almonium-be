package com.almonium.user.core.service.impl;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import java.util.Set;
import java.util.UUID;

public final class UserUtility {
    private UserUtility() {}

    public static User getUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("john");
        user.setEmail("john@example.com");
        Profile profile = new Profile();
        profile.setAvatarUrl("profile.jpg");
        user.setProfile(profile);
        Learner learner = Learner.builder().language(Language.EN).build();
        user.setLearners(Set.of(learner));
        user.setFluentLangs(Set.of(Language.ES, Language.RU));
        return user;
    }
}
