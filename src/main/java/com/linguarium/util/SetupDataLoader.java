package com.linguarium.util;

import com.linguarium.user.repository.LearnerRepository;
import com.linguarium.user.repository.ProfileRepository;
import com.linguarium.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("test")
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    @Autowired
    LearnerRepository learnerRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // setup data
        alreadySetup = true;
    }
}