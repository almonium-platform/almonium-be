package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.events.UserProfileUpdatedEvent;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.ProfileRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** A profile's avatar is one of the pictures the clients ship, or none. Nothing is uploaded anywhere. */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AvatarService {
    ProfileService profileService;
    ProfileRepository profileRepository;
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public void chooseDefaultAvatar(UUID id, String url) {
        Profile profile = profileService.getProfileById(id);
        updateProfileAvatarUrl(url, profile);
    }

    @Transactional
    public void resetCurrentAvatar(UUID id) {
        Profile profile = profileService.getProfileById(id);
        profile.setAvatarUrl(null);
        profileRepository.save(profile);
        log.info("Deleted current avatar for profile with id: {}", id);
    }

    @Transactional
    public void updateProfileAvatarUrl(String url, Profile profile) {
        profile.setAvatarUrl(url);
        Profile savedProfile = profileRepository.save(profile);
        eventPublisher.publishEvent(
                new UserProfileUpdatedEvent(savedProfile.getUser().getId()));

        log.info(
                "Updated profile avatar URL for user {} and published UserProfileUpdatedEvent.",
                savedProfile.getUser().getId());
    }
}
