package com.linguarium.user.service.impl;

import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import java.util.Set;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtility {
    public User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("password");
        user.setEmail("john@example.com");
        Profile profile = new Profile();
        profile.setUiLang(Language.EN);
        profile.setProfilePicLink("profile.jpg");
        profile.setBackground("background.jpg");
        profile.setStreak(5);
        user.setProfile(profile);
        Learner learner = new Learner();
        learner.setTargetLangs(Set.of(Language.DE.name(), Language.FR.name()));
        learner.setFluentLangs(Set.of(Language.ES.name(), Language.RU.name()));
        user.setLearner(learner);
        return user;
    }
}
