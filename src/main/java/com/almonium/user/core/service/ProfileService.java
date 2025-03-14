package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import com.almonium.user.core.dto.response.BaseProfileInfo;
import com.almonium.user.core.dto.response.FullProfileInfo;
import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.model.entity.Interest;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.record.RelationshipInfo;
import com.almonium.user.relationship.service.RelationshipService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfileService {
    RelationshipService relationshipService;
    PlanSubscriptionService planSubscriptionService;

    UserRepository userRepository;
    ProfileRepository profileRepository;
    PlanService planService;

    public void updateLoginStreak(Profile profile) {
        LocalDate lastLoginDate = profile.getLastLogin().toLocalDate();
        LocalDate currentDate = LocalDate.now();

        // If the user logs in the next day, increment the streak, otherwise reset it to 1
        profile.setStreak(lastLoginDate.plusDays(1).isEqual(currentDate) ? profile.getStreak() + 1 : 1);

        profile.setLastLogin(LocalDateTime.now());
        profileRepository.save(profile);
        log.info("Login streak updated for user: {}", profile.getId());
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

    @Transactional
    public BaseProfileInfo getUserProfileInfo(UUID viewer, UUID profileId) {
        User user = userRepository
                .findUserDetailsById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + profileId));

        Profile profile = profileRepository
                .findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found: " + profileId));

        RelationshipInfo relationshipInfo =
                relationshipService.getRelationshipInfo(viewer, profileId, profile.isHidden());

        if (!relationshipInfo.profileVisible()) {
            return getPublicProfileInfo(user, relationshipInfo);
        }

        FullProfileInfo fullUserInfo = getFullProfileInfo(user, relationshipInfo);
        fullUserInfo.setFriendshipId(relationshipInfo.friendshipId());
        fullUserInfo.setRelationshipStatus(relationshipInfo.status());

        return fullUserInfo;
    }

    private FullProfileInfo getFullProfileInfo(User user, RelationshipInfo relationshipInfo) {
        Profile profile = user.getProfile();
        BaseProfileInfo baseProfileInfo = getPublicProfileInfo(user, relationshipInfo);
        List<String> interests =
                user.getInterests().stream().map(Interest::getName).toList();

        FullProfileInfo fullUserInfo = new FullProfileInfo(baseProfileInfo);
        fullUserInfo.setFluentLangs(user.getFluentLangs());
        fullUserInfo.setTargetLangs(getUserTargetLangsWithProficiency(user));
        fullUserInfo.setInterests(interests);
        fullUserInfo.setLoginStreak(profile.getStreak());

        fullUserInfo.setFriendshipId(
                relationshipInfo.friendship().map(Relationship::getId).orElse(null));
        fullUserInfo.setRelationshipStatus(relationshipInfo.status());

        return fullUserInfo;
    }

    private BaseProfileInfo getPublicProfileInfo(User user, RelationshipInfo relationshipInfo) {
        Profile profile = user.getProfile();

        PlanSubscription activePlanSubscription = planSubscriptionService.getActiveSub(user);
        boolean isPremium =
                planService.isPlanPremium(activePlanSubscription.getPlan().getId());

        return BaseProfileInfo.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .isPremium(isPremium)
                .avatarUrl(profile.getAvatarUrl())
                .registeredAt(user.getRegistered())
                .relationshipStatus(relationshipInfo.status())
                .acceptsRequests(relationshipInfo.acceptsRequests())
                .build();
    }

    private List<TargetLanguageWithProficiency> getUserTargetLangsWithProficiency(User user) {
        return user.getLearners().stream()
                .map(learner ->
                        new TargetLanguageWithProficiency(learner.getLanguage(), learner.getSelfReportedLevel()))
                .toList();
    }
}
