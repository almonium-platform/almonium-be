package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.ProfileRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfileService {
    ProfileRepository profileRepository;

    public void updateLoginStreak(Profile profile) {
        LocalDate lastLoginDate = profile.getLastLogin().toLocalDate();
        LocalDate currentDate = LocalDate.now();

        // If the user logs in the next day, increment the streak, otherwise reset it to 1
        profile.setStreak(lastLoginDate.plusDays(1).isEqual(currentDate) ? profile.getStreak() + 1 : 1);

        profile.setLastLogin(LocalDateTime.now());
        profileRepository.save(profile);
    }

    public Profile getProfileById(Long id) {
        return profileRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotAccessibleException("Profile not found with id: " + id));
    }
}
