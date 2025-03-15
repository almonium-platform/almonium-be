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
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.record.RelationshipInfo;
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
    PlanSubscriptionService planSubscriptionService;
    PlanService planService;

    ProfileRepository profileRepository;
    UserRepository userRepository;

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

        fullUserInfo.setRelationshipId(
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
                .relationshipId(relationshipInfo.relationshipId())
                .build();
    }

    private List<TargetLanguageWithProficiency> getUserTargetLangsWithProficiency(User user) {
        return user.getLearners().stream()
                .map(learner ->
                        new TargetLanguageWithProficiency(learner.getLanguage(), learner.getSelfReportedLevel()))
                .toList();
    }
}
