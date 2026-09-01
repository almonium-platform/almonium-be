package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.subscription.model.entity.AccessGrant;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.model.record.UserEntitlement;
import com.almonium.subscription.repository.AccessGrantRepository;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.TestDataGenerator;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class EffectiveAccessServiceTest {
    private static final UUID GRANTED_ID = UUID.randomUUID();
    private static final UUID PAYING_ID = UUID.randomUUID();
    private static final UUID PLANLESS_ID = UUID.randomUUID();

    @Mock
    AccessGrantRepository accessGrantRepository;

    @Mock
    PlanSubscriptionService planSubscriptionService;

    @InjectMocks
    EffectiveAccessService effectiveAccessService;

    @DisplayName("Should answer with the grant rather than the plan when a grant is active")
    @Test
    void givenActiveGrant_whenEntitlementFor_thenGrantWins() {
        User user = TestDataGenerator.buildTestUserWithId(GRANTED_ID);
        when(accessGrantRepository.findActiveByUserId(any(), any()))
                .thenReturn(java.util.Optional.of(grant(user, Entitlement.PREMIUM)));

        assertThat(effectiveAccessService.entitlementFor(user)).isEqualTo(Entitlement.PREMIUM);
        assertThat(effectiveAccessService.isPremium(user)).isTrue();
        verify(planSubscriptionService, never()).getActivePlan(any());
    }

    @DisplayName("Should fall back to the plan when no grant is active")
    @Test
    void givenNoGrant_whenEntitlementFor_thenPlanAnswers() {
        User user = TestDataGenerator.buildTestUserWithId(PAYING_ID);
        when(accessGrantRepository.findActiveByUserId(any(), any())).thenReturn(java.util.Optional.empty());
        when(planSubscriptionService.getActivePlan(user)).thenReturn(plan(Entitlement.PREMIUM));

        assertThat(effectiveAccessService.isPremium(user)).isTrue();
    }

    @DisplayName("Should let a grant override a paid plan downwards")
    @Test
    void givenFreeGrantOverPremiumPlan_whenEntitlementsFor_thenGrantWins() {
        User user = TestDataGenerator.buildTestUserWithId(PAYING_ID);
        when(planSubscriptionService.getActiveEntitlements(anyCollection()))
                .thenReturn(List.of(new UserEntitlement(PAYING_ID, Entitlement.PREMIUM)));
        when(accessGrantRepository.findActiveByUserIds(anyCollection(), any()))
                .thenReturn(List.of(grant(user, Entitlement.FREE)));

        assertThat(effectiveAccessService.entitlementsFor(List.of(PAYING_ID)))
                .containsExactly(java.util.Map.entry(PAYING_ID, Entitlement.FREE));
        assertThat(effectiveAccessService.premiumAmong(List.of(PAYING_ID))).isEmpty();
    }

    @DisplayName("Should find the members in a list whether they were granted or they pay")
    @Test
    void givenMixedList_whenPremiumAmong_thenGrantAndPlanBothCount() {
        User granted = TestDataGenerator.buildTestUserWithId(GRANTED_ID);
        when(planSubscriptionService.getActiveEntitlements(anyCollection()))
                .thenReturn(List.of(
                        new UserEntitlement(PAYING_ID, Entitlement.PREMIUM),
                        new UserEntitlement(GRANTED_ID, Entitlement.FREE)));
        when(accessGrantRepository.findActiveByUserIds(anyCollection(), any()))
                .thenReturn(List.of(grant(granted, Entitlement.UNLIMITED)));

        Set<UUID> members = effectiveAccessService.premiumAmong(List.of(GRANTED_ID, PAYING_ID, PLANLESS_ID));

        assertThat(members).containsExactlyInAnyOrder(GRANTED_ID, PAYING_ID);
    }

    @DisplayName("Should not query anything for an empty list")
    @Test
    void givenNoUsers_whenEntitlementsFor_thenNothingIsQueried() {
        assertThat(effectiveAccessService.entitlementsFor(List.of())).isEmpty();

        verifyNoInteractions(accessGrantRepository, planSubscriptionService);
    }

    private AccessGrant grant(User user, Entitlement entitlement) {
        AccessGrant grant = new AccessGrant();
        grant.setUser(user);
        grant.setEntitlement(entitlement);
        grant.setStartsAt(Instant.now());
        return grant;
    }

    private Plan plan(Entitlement entitlement) {
        Plan plan = new Plan();
        plan.setEntitlement(entitlement);
        return plan;
    }
}
