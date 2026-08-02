package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.subscription.mapper.PlanSubscriptionMapper;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.subscription.service.StripeApiService;
import com.almonium.user.core.dto.response.SubscriptionInfoDto;
import com.almonium.user.core.dto.response.UserInfo;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.PlanService;
import com.almonium.user.core.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PlanSubscriptionService planSubscriptionService;

    @Mock
    PlanService planService;

    @Mock
    PlanValidationService planValidationService;

    @Mock
    EffectiveAccessService effectiveAccessService;

    @Mock
    PlanSubscriptionMapper planSubscriptionMapper;

    @Mock
    StripeApiService stripeApiService;

    @Mock
    UserMapper userMapper;

    @Mock
    StreamChatService streamChatService;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @DisplayName("Should change username")
    @Test
    void givenUsername_whenChangeUsername_thenUsernameByIdChanged() {
        String username = "username";
        String newUsername = "new_username";
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).username(username).build();

        when(userRepository.existsByUsername(newUsername)).thenReturn(false);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.changeUsernameById(newUsername, id);

        verify(userRepository).findById(id);
        verify(userRepository).existsByUsername(newUsername);
        verify(userRepository).save(any(User.class));
    }

    @DisplayName("Should return user optional for existing user")
    @Test
    void givenExistingUser_whenFindUserById_thenReturnUserOptional() {
        UUID userId = UUID.randomUUID();
        User user = UserUtility.getUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getById(userId);

        assertThat(result).isEqualTo(user);
        verify(userRepository).findById(userId);
    }

    @DisplayName("Should return empty optional for non existing user")
    @Test
    void givenNonExistingUser_whenFindUserById_thenThrowEntityNotFoundException() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(userId)).isInstanceOf(EntityNotFoundException.class);
        verify(userRepository).findById(userId);
    }
    //
    //    @DisplayName("Should delete user account")
    //    @Test
    //    void givenUser_whenDeleteAccount_thenRepositoryDeleteIsCalled() {
    //        User user = UserUtility.getUser();
    //        when(planSubscriptionService.findActiveSubscription(user)).thenReturn(Optional.empty());
    //        userService.deleteAccount(user);
    //
    //        verify(userRepository).delete(user);
    //    }
    //
    //    @DisplayName("Should delete user account")
    //    @Test
    //    void givenUserWithActiveSubscription_whenDeleteAccount_thenRepositoryDeleteIsCalledAndSubscriptionCancelled()
    // {
    //        User user = UserUtility.getUser();
    //        String subscriptionId = "sub_123";
    //        when(planSubscriptionService.findActiveSubscription(user))
    //                .thenReturn(Optional.of(PlanSubscription.builder()
    //                        .stripeSubscriptionId(subscriptionId)
    //                        .user(user)
    //                        .build()));
    //        userService.deleteAccount(user);
    //
    //        verify(stripeApiService).cancelSubscription(subscriptionId);
    //        verify(userRepository).delete(user);
    //    }

    @DisplayName("Should return user if email exists")
    @Test
    void givenExistentEmail_whenFindByEmail_thenReturnUser() {
        String email = "john@example.com";
        User expectedUser = UserUtility.getUser();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findByEmail(email).orElseThrow();

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @DisplayName("Should return null if email doesn't exist")
    @Test
    void givenNonExistentEmail_whenFindByEmail_thenReturnNull() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.findByEmail(email);

        assertThat(actualUser).isEmpty();
    }

    @DisplayName("Should map user to user info")
    @Test
    void givenLocalUser_whenBuildUserInfo_thenMapFieldsAndCallNecessaryServices() {
        User user = UserUtility.getUser();
        user.setEmail("john@example.com");
        user.setId(UUID.randomUUID());
        user.setStreamChatToken(null);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // plan
        long planId = 22L;
        Plan plan = Plan.builder().id(planId).name("Premium Plan").build();
        PlanSubscription planSubscription = PlanSubscription.builder()
                .plan(plan)
                .status(PlanSubscription.Status.ACTIVE)
                .build();

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(user.getEmail());
        SubscriptionInfoDto subscriptionInfoDto = new SubscriptionInfoDto();
        subscriptionInfoDto.setName(plan.getName());

        when(planSubscriptionService.getActiveSub(user)).thenReturn(planSubscription);
        when(effectiveAccessService.entitlementFor(user)).thenReturn(Entitlement.PREMIUM);
        when(planService.getPlanLimits(Entitlement.PREMIUM)).thenReturn(Map.of((PlanFeature.MAX_TARGET_LANGS), 3));
        when(userMapper.userToUserInfo(user)).thenReturn(userInfo);
        when(streamChatService.generateStreamToken(user)).thenReturn("fresh-stream-token");
        when(planSubscriptionMapper.planSubscriptionToPlanDto(eq(planSubscription)))
                .thenReturn(subscriptionInfoDto);

        UserInfo result = userService.buildUserInfoFromUser(user);

        verify(userMapper).userToUserInfo(user);
        verify(streamChatService).generateStreamToken(user);
        verify(planSubscriptionMapper).planSubscriptionToPlanDto(planSubscription);
        verify(planService).getPlanLimits(Entitlement.PREMIUM);

        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getStreamChatToken()).isEqualTo("fresh-stream-token");
        assertThat(result.getSubscription().getName()).isEqualTo("Premium Plan");
        assertThat(result.getSubscription().getLimits()).containsEntry(PlanFeature.MAX_TARGET_LANGS, 3);
        assertThat(result.isPremium()).isTrue();
    }

    @DisplayName("Should return true when username is available")
    @Test
    void givenAvailableUsername_whenIsUsernameAvailable_thenReturnsTrue() {
        // Arrange
        String username = "username";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = userService.isUsernameAvailable(username);

        // Assert
        assertThat(result).isTrue();
    }

    @DisplayName("Should return false when username is already taken")
    @Test
    void givenTakenUsername_whenIsUsernameAvailable_thenReturnsFalse() {
        // Arrange
        String username = "existing_username";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = userService.isUsernameAvailable(username);

        // Assert
        assertThat(result).isFalse();
    }

    @DisplayName("Should validate the fluent language count against the active plan")
    @Test
    void givenFluentLanguages_whenUpdateFluentLanguages_thenValidatesPlanLimitAndSaves() {
        User user = UserUtility.getUser();
        Set<Language> fluentLanguages = Set.of(Language.EN, Language.DE);

        userService.updateFluentLanguages(fluentLanguages, user);

        verify(planValidationService).validatePlanFeature(user, PlanFeature.MAX_FLUENT_LANGS, fluentLanguages.size());
        verify(userRepository).save(user);
        assertThat(user.getFluentLangs()).containsExactlyInAnyOrderElementsOf(fluentLanguages);
    }
}
