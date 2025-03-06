package com.almonium.user.core.service.impl;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtility {
    public User getUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("john");
        user.setEmail("john@example.com");
        Profile profile = new Profile();
        profile.setAvatarUrl("profile.jpg");
        profile.setStreak(5);
        user.setProfile(profile);
        Learner learner = Learner.builder().language(Language.EN).build();
        user.setLearners(List.of(learner));
        user.setFluentLangs(Set.of(Language.ES, Language.RU));
        user.setLearners(List.of(learner));
        return user;
    }
}
