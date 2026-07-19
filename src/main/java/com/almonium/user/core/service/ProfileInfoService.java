package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import com.almonium.user.core.dto.response.BaseProfileInfo;
import com.almonium.user.core.dto.response.FullProfileInfo;
import com.almonium.user.core.model.entity.Interest;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.relationship.model.record.RelationshipPerspective;
import com.almonium.user.relationship.service.RelationshipPerspectiveResolver;
import com.almonium.user.relationship.service.RelationshipService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
public class ProfileInfoService {
    RelationshipService relationshipService;
    RelationshipPerspectiveResolver relationshipPerspectiveResolver;
    PlanSubscriptionService planSubscriptionService;
    PlanService planService;

    ProfileRepository profileRepository;
    UserRepository userRepository;

    @Transactional
    public BaseProfileInfo getUserProfileInfo(UUID viewer, UUID profileId) {
        User user = findUserById(profileId);
        Profile profile = findProfileById(profileId);

        RelationshipPerspective relationshipPerspective =
                relationshipService.getRelationshipPerspective(viewer, user, profile.isHidden());

        return getProfileBasedOnVisibility(user, relationshipPerspective);
    }

    @Transactional
    public BaseProfileInfo getPublicProfileInfo(UUID profileId) {
        User user = findUserById(profileId);
        Profile profile = findProfileById(profileId);

        RelationshipPerspective relationshipPerspective =
                relationshipPerspectiveResolver.stranger(user, profile.isHidden());

        return getProfileBasedOnVisibility(user, relationshipPerspective);
    }

    @Transactional
    public BaseProfileInfo getPublicProfileInfoByUsername(String username) {
        User user = userRepository
                .findUserDetailsByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        Profile profile = findProfileById(user.getId());

        RelationshipPerspective relationshipPerspective =
                relationshipPerspectiveResolver.stranger(user, profile.isHidden());

        return getProfileBasedOnVisibility(user, relationshipPerspective);
    }

    private User findUserById(UUID userId) {
        return userRepository
                .findUserDetailsById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private Profile findProfileById(UUID profileId) {
        return profileRepository
                .findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found: " + profileId));
    }

    private BaseProfileInfo getProfileBasedOnVisibility(User user, RelationshipPerspective relationshipPerspective) {
        if (!relationshipPerspective.profileVisible()) {
            return getPublicProfileInfo(user, relationshipPerspective);
        }

        FullProfileInfo fullUserInfo = getFullProfileInfo(user, relationshipPerspective);
        fullUserInfo.setRelationshipStatus(relationshipPerspective.status());

        return fullUserInfo;
    }

    private FullProfileInfo getFullProfileInfo(User user, RelationshipPerspective relationshipPerspective) {
        Profile profile = user.getProfile();
        BaseProfileInfo baseProfileInfo = getPublicProfileInfo(user, relationshipPerspective);
        List<String> interests =
                user.getInterests().stream().map(Interest::getName).toList();

        FullProfileInfo fullUserInfo = new FullProfileInfo(baseProfileInfo);
        fullUserInfo.setFluentLangs(user.getFluentLangs());
        fullUserInfo.setTargetLangs(getUserTargetLangsWithProficiency(user));
        fullUserInfo.setInterests(interests);
        fullUserInfo.setLoginStreak(profile.getStreak());

        fullUserInfo.setRelationshipId(relationshipPerspective.relationshipId());
        fullUserInfo.setRelationshipStatus(relationshipPerspective.status());

        return fullUserInfo;
    }

    private BaseProfileInfo getPublicProfileInfo(User user, RelationshipPerspective relationshipPerspective) {
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
                .relationshipStatus(relationshipPerspective.status())
                .acceptsRequests(relationshipPerspective.acceptsRequests())
                .relationshipId(relationshipPerspective.relationshipId())
                .build();
    }

    private List<TargetLanguageWithProficiency> getUserTargetLangsWithProficiency(User user) {
        return user.getLearners().stream()
                .map(learner ->
                        new TargetLanguageWithProficiency(learner.getLanguage(), learner.getSelfReportedLevel()))
                .toList();
    }
}
