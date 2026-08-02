package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.subscription.mapper.PlanSubscriptionMapper;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.dto.response.SubscriptionInfoDto;
import com.almonium.user.core.dto.response.UserInfo;
import com.almonium.user.core.events.UserProfileUpdatedEvent;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.InterestRepository;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class UserService {
    PlanSubscriptionService planSubscriptionService;
    EffectiveAccessService effectiveAccessService;
    PlanService planService;
    PlanValidationService planValidationService;

    UserRepository userRepository;
    InterestRepository interestRepository;

    PlanSubscriptionMapper planSubscriptionMapper;
    UserMapper userMapper;
    StreamChatService streamChatService;

    ApplicationEventPublisher eventPublisher;

    public UserInfo buildUserInfoFromUser(User user) {
        User fetchedUser = getByEmail(user.getEmail());
        PlanSubscription activePlanSubscription = planSubscriptionService.getActiveSub(user);
        Map<PlanFeature, Integer> limits = planService.getPlanLimits(effectiveAccessService.entitlementFor(user));
        var userInfo = userMapper.userToUserInfo(fetchedUser);
        userInfo.setStreamChatToken(streamChatService.generateStreamToken(fetchedUser));
        SubscriptionInfoDto subscriptionInfoDto =
                planSubscriptionMapper.planSubscriptionToPlanDto(activePlanSubscription);
        userInfo.setSubscription(subscriptionInfoDto);
        userInfo.getSubscription().setLimits(limits);
        userInfo.setPremium(effectiveAccessService.entitlementFor(user)
                != com.almonium.subscription.model.entity.enums.Entitlement.FREE);
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
        eventPublisher.publishEvent(new UserProfileUpdatedEvent(id));
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

    public void updateFluentLanguages(Set<Language> langs, User user) {
        planValidationService.validatePlanFeature(user, PlanFeature.MAX_FLUENT_LANGS, langs.size());
        user.setFluentLangs(new HashSet<>(langs));
        userRepository.save(user);
    }
}
