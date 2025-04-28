package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.storage.service.FirebaseStorageService;
import com.almonium.user.core.dto.response.AvatarDto;
import com.almonium.user.core.events.UserProfileUpdatedEvent;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.FirebaseIntegrationException;
import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.mapper.AvatarMapper;
import com.almonium.user.core.model.entity.Avatar;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.AvatarRepository;
import com.almonium.user.core.repository.ProfileRepository;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AvatarService {
    private static final String FIREBASE_STORAGE_DOMAIN = "https://firebasestorage.googleapis.com";
    private static final String AVATAR_PREFIX = "avatars/users/";
    private static final String DEFAULT_AVATAR_PREFIX = AVATAR_PREFIX + "default";
    private static final Pattern AVATAR_URL_PATTERN = Pattern.compile(".*/o/" + AVATAR_PREFIX + "([^/?]+).*");
    private static final String PATH_FORMAT = "%s%s";

    FirebaseStorageService firebaseStorageService;
    ProfileService profileService;

    AvatarRepository avatarRepository;
    ProfileRepository profileRepository;

    AvatarMapper avatarMapper;
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public void addAndSetNewCustomAvatar(UUID id, String url) {
        Profile profile = profileService.getProfileById(id);
        avatarRepository.findByProfileIdAndUrl(id, url).ifPresent(existingAvatar -> {
            throw new IllegalArgumentException("Avatar already exists");
        });
        avatarRepository.save(new Avatar(profile, url));
        updateProfileAvatarUrl(url, profile);
    }

    @Transactional
    public void chooseExistingCustomAvatar(UUID id, UUID avatarId) {
        var avatar = getMyAvatar(id, avatarId);
        Profile profile = profileService.getProfileById(id);
        updateProfileAvatarUrl(avatar.getUrl(), profile);
    }

    public List<String> getAvatarPathsForUser(UUID userId) {
        return avatarRepository.findAllByProfileId(userId).stream()
                .map(Avatar::getUrl)
                .map(this::extractPathFromUrl)
                .collect(Collectors.toList());
    }

    public List<AvatarDto> getAvatars(UUID id) {
        return avatarMapper.toDto(avatarRepository.findAllByProfileId(id));
    }

    @Transactional
    public void resetCurrentAvatar(UUID id) {
        Profile profile = profileService.getProfileById(id);
        resetCurrentAvatar(id, profile);
    }

    @Transactional
    public void deleteCustomAvatar(UUID id, UUID avatarId) {
        var avatar = getMyAvatar(id, avatarId);
        var profile = profileService.getProfileById(id);

        String url = avatar.getUrl();
        if (url.startsWith(FIREBASE_STORAGE_DOMAIN)) {
            firebaseStorageService.deleteFile(extractPathFromUrl(url));
        }
        avatarRepository.delete(avatar);

        if (url.equals(profile.getAvatarUrl())) {
            resetCurrentAvatar(id, profile);
        }
    }

    @Transactional
    public void chooseDefaultAvatar(UUID id, String url) {
        Profile profile = profileService.getProfileById(id);
        updateProfileAvatarUrl(url, profile);
    }

    @Transactional
    public void doAvatarUpload(String remoteUrl, UUID profileId) {
        if (remoteUrl == null) {
            return;
        }
        try (InputStream in = new URL(remoteUrl).openStream()) {
            byte[] avatarBytes = in.readAllBytes();

            String filePath = generateFilePath(UUID.randomUUID());

            String uploadedUrl = firebaseStorageService.upload(avatarBytes, MediaType.IMAGE_JPEG_VALUE, filePath);

            addAndSetNewCustomAvatar(profileId, uploadedUrl);
        } catch (Exception e) {
            log.error("Failed to upload avatar from remote url: {}", remoteUrl, e);
        }
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

    private Avatar getMyAvatar(UUID id, UUID avatarId) {
        return avatarRepository
                .findById(avatarId)
                .filter(avatar -> avatar.getProfile().getId().equals(id))
                .orElseThrow(() -> new ResourceNotAccessibleException("Avatar not found with id: " + avatarId));
    }

    private String extractPathFromUrl(String url) {
        String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
        if (decodedUrl.contains(DEFAULT_AVATAR_PREFIX)) {
            throw new BadUserRequestActionException("Cannot delete default avatar");
        }

        Matcher matcher = AVATAR_URL_PATTERN.matcher(decodedUrl);

        if (matcher.matches()) {
            String uuid = matcher.group(1);
            return generateFilePath(UUID.fromString(uuid));
        }
        throw new FirebaseIntegrationException("Invalid avatar URL format");
    }

    private String generateFilePath(UUID uuid) {
        return String.format(PATH_FORMAT, AVATAR_PREFIX, uuid);
    }

    private void resetCurrentAvatar(UUID id, Profile profile) {
        profile.setAvatarUrl(null);
        profileRepository.save(profile);
        log.info("Deleted current avatar for profile with id: {}", id);
    }
}
