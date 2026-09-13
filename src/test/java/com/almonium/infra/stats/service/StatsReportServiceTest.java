package com.almonium.infra.stats.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.almonium.infra.stats.dto.StatsReport;
import com.almonium.subscription.model.entity.FoundingMember;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.model.record.GrantCount;
import com.almonium.subscription.model.record.SubscriptionCount;
import com.almonium.subscription.repository.AccessGrantRepository;
import com.almonium.subscription.repository.FoundingMemberRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.user.core.repository.UserRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatsReportServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    ProfileRepository profileRepository;

    @Mock
    PlanSubscriptionRepository subscriptionRepository;

    @Mock
    AccessGrantRepository grantRepository;

    @Mock
    FoundingMemberRepository foundingMemberRepository;

    @InjectMocks
    StatsReportService service;

    @Test
    void putsEveryCountOnOnePage() {
        when(userRepository.count()).thenReturn(120L);
        when(userRepository.countByRegisteredGreaterThanEqual(any())).thenReturn(3L, 10L, 40L);
        when(profileRepository.countByLastSeenAtGreaterThanEqual(any())).thenReturn(8L, 25L, 60L);
        when(subscriptionRepository.countByPlanAndStatus())
                .thenReturn(List.of(
                        new SubscriptionCount("PREMIUM", Plan.Type.MONTHLY, PlanSubscription.Status.ACTIVE, 5)));
        when(grantRepository.countActiveByEntitlement(any()))
                .thenReturn(List.of(new GrantCount(Entitlement.PREMIUM, 4)));
        when(grantRepository.countActivePremiumWithoutPaidPlan(any())).thenReturn(3L);
        when(foundingMemberRepository.countByStatus(FoundingMember.Status.CONFIRMED))
                .thenReturn(2L);
        when(foundingMemberRepository.countByStatus(FoundingMember.Status.RESERVED))
                .thenReturn(1L);
        when(foundingMemberRepository.countByStatus(FoundingMember.Status.AVAILABLE))
                .thenReturn(97L);

        StatsReport report = service.report();

        assertThat(report.totalUsers()).isEqualTo(120);
        assertThat(report.windows())
                .extracting(StatsReport.Window::label, StatsReport.Window::registered, StatsReport.Window::active)
                .containsExactly(tuple("today", 3L, 8L), tuple("7 days", 10L, 25L), tuple("30 days", 40L, 60L));
        assertThat(report.subscriptions()).hasSize(1);
        assertThat(report.activeGrants()).containsExactly(new GrantCount(Entitlement.PREMIUM, 4));
        assertThat(report.grantOnlyMembers()).isEqualTo(3);
        assertThat(report.foundingMembers()).isEqualTo(new StatsReport.FoundingMembers(2, 1, 97));
    }

    @Test
    void todayStartsAtUtcMidnightAndTheRestTrailTheClock() {
        StatsReport report = service.report();

        Instant startOfToday =
                LocalDate.now(ZoneOffset.UTC).atStartOfDay(ZoneOffset.UTC).toInstant();
        assertThat(report.windows().get(0).since()).isEqualTo(startOfToday);
        assertThat(Duration.between(report.windows().get(1).since(), report.generatedAt()))
                .isEqualTo(Duration.ofDays(7));
        assertThat(Duration.between(report.windows().get(2).since(), report.generatedAt()))
                .isEqualTo(Duration.ofDays(30));
    }
}
