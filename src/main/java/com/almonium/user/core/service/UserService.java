package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.model.entity.Principal;
import com.almonium.auth.local.model.entity.LocalPrincipal;
import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.subscription.mapper.PlanSubscriptionMapper;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import com.almonium.user.core.dto.response.BaseUserInfo;
import com.almonium.user.core.dto.response.FullUserInfo;
import com.almonium.user.core.dto.response.SubscriptionInfoDto;
import com.almonium.user.core.dto.response.UserInfo;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.exception.NoPrincipalFoundException;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.Interest;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.InterestRepository;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.record.RelationshipInfo;
import com.almonium.user.friendship.service.FriendshipService;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class UserService implements UserDetailsService {
    PlanSubscriptionService planSubscriptionService;
    FriendshipService friendshipService;
    StreamChatService streamChatService;
    PlanService planService;

    UserRepository userRepository;
    ProfileRepository profileRepository;
    InterestRepository interestRepository;

    PlanSubscriptionMapper planSubscriptionMapper;
    UserMapper userMapper;

    @Transactional
    public BaseUserInfo getUserProfileInfo(UUID viewer, UUID profileId) {
        User user = userRepository
                .findUserDetailsById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + profileId));

        Profile profile = profileRepository
                .findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found: " + profileId));

        RelationshipInfo relationshipInfo = friendshipService.getRelationshipInfo(viewer, profileId);

        boolean isVisible = relationshipInfo.canViewFullProfile(profile.isHidden());

        if (!isVisible) {
            return getPublicProfileInfo(user);
        }

        FullUserInfo fullUserInfo = getFullProfileInfo(user, relationshipInfo);
        fullUserInfo.setFriendshipId(relationshipInfo.friendshipId());
        fullUserInfo.setRelationshipStatus(relationshipInfo.status());

        return fullUserInfo;
    }

    public UserInfo buildUserInfoFromUser(User user) {
        User fetchedUser = getByEmail(user.getEmail());
        PlanSubscription activePlanSubscription = planSubscriptionService.getActiveSub(user);
        Map<PlanFeature, Integer> limits =
                planService.getPlanLimits(activePlanSubscription.getPlan().getId());
        var userInfo = userMapper.userToUserInfo(fetchedUser);
        SubscriptionInfoDto subscriptionInfoDto =
                planSubscriptionMapper.planSubscriptionToPlanDto(activePlanSubscription);
        userInfo.setSubscription(subscriptionInfoDto);
        userInfo.getSubscription().setLimits(limits);
        userInfo.setPremium(
                planService.isPlanPremium(activePlanSubscription.getPlan().getId()));
        return userInfo;
    }

    public User getByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    // TODO fix. Eagerly fetches all user details, may be redundant for some use cases
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public void changeUsernameById(String username, UUID id) {
        username = username.toLowerCase();
        User user = getById(id);
        if (user.getUsername().equals(username)) {
            throw new BadUserRequestActionException("Username is already set to: " + username);
        }
        if (!isUsernameAvailable(username)) {
            throw new BadUserRequestActionException("Username is already taken: " + username);
        }
        user.setUsername(username);
        userRepository.save(user);
        streamChatService.updateUser(user);
        log.info("User {} changed username to: {}", id, username);
    }

    public void updateInterests(User user, Set<Long> ids) {
        user.setInterests(new HashSet<>(interestRepository.findAllById(ids)));
        userRepository.save(user);
        log.info("User {} updated interests: {}", user.getId(), ids);
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username.toLowerCase());
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return findByEmail(email)
                .map(user -> {
                    if (user.getPrincipals().isEmpty()) {
                        throw new NoPrincipalFoundException("User exists without any principals: " + email);
                    }

                    return getLocalPrincipal(user)
                            .orElseThrow(() -> new NoPrincipalFoundException(
                                    "Use %s to access your account instead of email and password."
                                            .formatted(collectProvidersNames(user))));
                })
                .orElseThrow(() -> new BadCredentialsException("Email or password are incorrect"));
    }

    public User getUserWithPrincipals(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    public Optional<LocalPrincipal> getLocalPrincipal(User user) {
        return user.getPrincipals().stream()
                .filter(principal -> principal instanceof LocalPrincipal)
                // to omit ephemeral principal waiting for email confirmation
                .filter(principal -> principal.getEmail().equals(user.getEmail()))
                .map(principal -> (LocalPrincipal) principal)
                .findFirst();
    }

    public Optional<LocalPrincipal> getUnverifiedLocalPrincipal(User user) {
        return user.getPrincipals().stream()
                .filter(principal -> principal instanceof LocalPrincipal)
                .filter(principal -> !principal.getEmail().equals(user.getEmail()))
                .map(principal -> (LocalPrincipal) principal)
                .findFirst();
    }

    public void updateFluentLanguages(Set<Language> langs, User user) {
        user.setFluentLangs(new HashSet<>(langs));
        userRepository.save(user);
    }

    private BaseUserInfo getPublicProfileInfo(User user) {
        Profile profile = user.getProfile();

        PlanSubscription activePlanSubscription = planSubscriptionService.getActiveSub(user);
        boolean isPremium =
                planService.isPlanPremium(activePlanSubscription.getPlan().getId());

        return BaseUserInfo.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .isPremium(isPremium)
                .avatarUrl(profile.getAvatarUrl())
                .registeredAt(user.getRegistered())
                .build();
    }

    private FullUserInfo getFullProfileInfo(User user, RelationshipInfo relationshipInfo) {
        Profile profile = user.getProfile();
        BaseUserInfo baseUserInfo = getPublicProfileInfo(user);
        List<String> interests =
                user.getInterests().stream().map(Interest::getName).toList();

        FullUserInfo fullUserInfo = new FullUserInfo(baseUserInfo);
        fullUserInfo.setFluentLangs(user.getFluentLangs());
        fullUserInfo.setTargetLangs(getUserTargetLangsWithProficiency(user));
        fullUserInfo.setInterests(interests);
        fullUserInfo.setLoginStreak(profile.getStreak());

        fullUserInfo.setFriendshipId(
                relationshipInfo.friendship().map(Friendship::getId).orElse(null));
        fullUserInfo.setRelationshipStatus(relationshipInfo.status());

        return fullUserInfo;
    }

    private List<TargetLanguageWithProficiency> getUserTargetLangsWithProficiency(User user) {
        return user.getLearners().stream()
                .map(learner ->
                        new TargetLanguageWithProficiency(learner.getLanguage(), learner.getSelfReportedLevel()))
                .toList();
    }

    private static String collectProvidersNames(User user) {
        return user.getPrincipals().stream()
                .map(Principal::getProvider)
                .map(Enum::name)
                .map(provider -> provider.substring(0, 1).toUpperCase()
                        + provider.substring(1).toLowerCase())
                .collect(Collectors.joining(", "));
    }
}
