package com.linguarium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.user.core.model.Profile;
import com.linguarium.user.core.repository.ProfileRepository;
import com.linguarium.user.core.service.ProfileService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfileServiceImpl implements ProfileService {
    ProfileRepository profileRepository;

    @Override
    public void updateLoginStreak(Profile profile) {
        LocalDate lastLoginDate = profile.getLastLogin().toLocalDate();
        LocalDate currentDate = LocalDate.now();

        profile.setStreak(lastLoginDate.plusDays(1).isEqual(currentDate) ? profile.getStreak() + 1 : 1);

        profile.setLastLogin(LocalDateTime.now());
        profileRepository.save(profile);
    }
}
