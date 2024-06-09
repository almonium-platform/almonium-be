package com.almonium.user.core.service.impl;

import com.almonium.engine.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtility {
    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");
        Profile profile = new Profile();
        profile.setUiLang(Language.EN);
        profile.setAvatarUrl("profile.jpg");
        profile.setBackground("background.jpg");
        profile.setStreak(5);
        user.setProfile(profile);
        Learner learner = new Learner();
        learner.setTargetLangs(Set.of(Language.DE, Language.FR));
        learner.setFluentLangs(Set.of(Language.ES, Language.RU));
        user.setLearner(learner);
        return user;
    }
}
