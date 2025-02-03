package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.ProfileRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
        log.info("Login streak updated for user: {}", profile.getId());
    }

    public void updateUIPreferences(Long userId, Map<String, Object> uiPreferences) {
        Profile profile = getProfileById(userId);
        profile.setUiPreferences(uiPreferences);
        profileRepository.save(profile);
        log.info("UI preferences updated for user: {}", userId);
    }

    public Profile getProfileById(Long id) {
        return profileRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotAccessibleException("Profile not found with id: " + id));
    }

    public void updateHidden(long userId, boolean hidden) {
        Profile profile = getProfileById(userId);
        profile.setHidden(hidden);
        profileRepository.save(profile);
        log.info("Hidden status updated for user: {}", userId);
    }
}
