package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.storage.service.FirebaseStorageService;
import com.almonium.user.core.dto.AvatarDto;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.FirebaseIntegrationException;
import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.mapper.AvatarMapper;
import com.almonium.user.core.model.entity.Avatar;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.AvatarRepository;
import com.almonium.user.core.repository.ProfileRepository;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AvatarService {
    private static final String FIREBASE_STORAGE_DOMAIN = "https://firebasestorage.googleapis.com";
    private static final String AVATAR_PREFIX = "avatars/";
    private static final String DEFAULT_AVATAR_PREFIX = AVATAR_PREFIX + "default";
    private static final Pattern AVATAR_URL_PATTERN = Pattern.compile(".*/o/" + AVATAR_PREFIX + "([^/?]+).*");

    AvatarRepository avatarRepository;
    FirebaseStorageService firebaseStorageService;
    AvatarMapper avatarMapper;
    ProfileService profileService;
    ProfileRepository profileRepository;

    @Transactional
    public void addAndSetNewCustomAvatar(Long id, String url) {
        Profile profile = profileService.getProfileById(id);
        avatarRepository.findByProfileIdAndUrl(id, url).ifPresent(existingAvatar -> {
            throw new IllegalArgumentException("Avatar already exists");
        });
        avatarRepository.save(new Avatar(profile, url));
        updateProfileAvatarUrl(url, profile);
    }

    public void chooseExistingCustomAvatar(Long id, Long avatarId) {
        var avatar = getMyAvatar(id, avatarId);
        Profile profile = profileService.getProfileById(id);
        updateProfileAvatarUrl(avatar.getUrl(), profile);
    }

    public void cleanUpAvatars(Long id) {
        avatarRepository
                .findAllByProfileId(id)
                .forEach(avatar -> firebaseStorageService.deleteFile(extractPathFromUrl(avatar.getUrl())));
        avatarRepository.deleteAllByProfileId(id);
    }

    public List<AvatarDto> getAvatars(Long id) {
        return avatarMapper.toDto(avatarRepository.findAllByProfileId(id));
    }

    public void resetCurrentAvatar(Long id) {
        Profile profile = profileService.getProfileById(id);
        resetCurrentAvatar(id, profile);
    }

    @Transactional
    public void deleteCustomAvatar(Long id, Long avatarId) {
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

    public void chooseDefaultAvatar(Long id, String url) {
        Profile profile = profileService.getProfileById(id);
        updateProfileAvatarUrl(url, profile);
    }

    private void updateProfileAvatarUrl(String url, Profile profile) {
        profile.setAvatarUrl(url);
        profileRepository.save(profile);
    }

    private Avatar getMyAvatar(Long id, Long avatarId) {
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
            String uuid = matcher.group(1); // Extract UUID directly
            return AVATAR_PREFIX + uuid; // Concatenate with prefix
        }
        throw new FirebaseIntegrationException("Invalid avatar URL format");
    }

    private void resetCurrentAvatar(Long id, Profile profile) {
        profile.setAvatarUrl(null);
        profileRepository.save(profile);
        log.info("Deleted current avatar for profile with id: {}", id);
    }
}
