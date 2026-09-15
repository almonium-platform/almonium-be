package com.almonium.infra.stats.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.stats.dto.StatsReport;
import com.almonium.infra.stats.dto.StatsReport.FoundingMembers;
import com.almonium.infra.stats.dto.StatsReport.Window;
import com.almonium.subscription.model.entity.FoundingMember;
import com.almonium.subscription.repository.AccessGrantRepository;
import com.almonium.subscription.repository.FoundingMemberRepository;
import com.almonium.subscription.repository.PlanSubscriptionRepository;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.user.core.repository.UserRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Counts the product's people from our own tables. See {@link StatsReport} for what each number means. */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StatsReportService {
    static final List<Integer> TRAILING_DAYS = List.of(7, 30);

    UserRepository userRepository;
    ProfileRepository profileRepository;
    PlanSubscriptionRepository subscriptionRepository;
    AccessGrantRepository grantRepository;
    FoundingMemberRepository foundingMemberRepository;

    @Transactional(readOnly = true)
    public StatsReport report() {
        Instant now = Instant.now();
        Instant startOfToday =
                LocalDate.now(ZoneOffset.UTC).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<Window> windows = new ArrayList<>();
        windows.add(window("today", startOfToday));
        TRAILING_DAYS.forEach(days -> windows.add(window(days + " days", now.minus(Duration.ofDays(days)))));

        return new StatsReport(
                now,
                userRepository.count(),
                List.copyOf(windows),
                subscriptionRepository.countByPlanAndStatus(),
                grantRepository.countActiveByEntitlement(now),
                grantRepository.countActivePremiumWithoutPaidPlan(now),
                new FoundingMembers(
                        foundingMemberRepository.countByStatus(FoundingMember.Status.CONFIRMED),
                        foundingMemberRepository.countByStatus(FoundingMember.Status.RESERVED),
                        foundingMemberRepository.countByStatus(FoundingMember.Status.AVAILABLE)));
    }

    private Window window(String label, Instant since) {
        return new Window(
                label,
                since,
                userRepository.countByRegisteredGreaterThanEqual(since),
                profileRepository.countByLastSeenAtGreaterThanEqual(since));
    }
}
