package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.ProfileRepository;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
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

    public void updateLastLogin(Profile profile) {
        profile.setLastLogin(LocalDateTime.now());
        profileRepository.save(profile);
        log.info("Last login updated for user: {}", profile.getId());
    }

    public void updateUIPreferences(UUID userId, Map<String, Object> uiPreferences) {
        Profile profile = getProfileById(userId);
        profile.setUiPreferences(uiPreferences);
        profileRepository.save(profile);
        log.info("UI preferences updated for user: {}", userId);
    }

    public Profile getProfileById(UUID id) {
        return profileRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotAccessibleException("Profile not found with id: " + id));
    }

    public void updateHidden(UUID userId, boolean hidden) {
        Profile profile = getProfileById(userId);
        profile.setHidden(hidden);
        profileRepository.save(profile);
        log.info("Hidden status updated for user: {}", userId);
    }

    public void updateSocialEmailNotifications(UUID userId, boolean enabled) {
        Profile profile = getProfileById(userId);
        profile.setSocialEmailNotifications(enabled);
        profileRepository.save(profile);
        log.info("Social email notifications {} for user: {}", enabled ? "enabled" : "disabled", userId);
    }
}
