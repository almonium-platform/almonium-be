package com.almonium.subscription.repository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.config.PostgresContainer;
import com.almonium.subscription.model.entity.Plan;
import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.model.record.SubscriptionCount;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@FieldDefaults(level = PRIVATE)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PlanSubscriptionRepositoryTest {
    private static final UUID JOHN_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");
    private static final UUID JANE_ID = UUID.fromString("01956cde-a541-7ac1-8b32-2896d096ecdf");
    /** Seeded by Liquibase in the test context: FREE lifetime, PREMIUM monthly, PREMIUM yearly. */
    private static final long FREE_PLAN = 1L;

    private static final long PREMIUM_MONTHLY = 2L;

    @Autowired
    PlanSubscriptionRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    void countsSubscriptionsByPlanAndStatus() {
        repository.saveAndFlush(subscription(JOHN_ID, PREMIUM_MONTHLY, PlanSubscription.Status.ACTIVE));
        repository.saveAndFlush(subscription(JANE_ID, PREMIUM_MONTHLY, PlanSubscription.Status.ACTIVE));
        repository.saveAndFlush(subscription(JANE_ID, FREE_PLAN, PlanSubscription.Status.CANCELED));

        assertThat(repository.countByPlanAndStatus())
                .containsExactly(
                        new SubscriptionCount("FREE", Plan.Type.LIFETIME, PlanSubscription.Status.CANCELED, 1),
                        new SubscriptionCount("PREMIUM", Plan.Type.MONTHLY, PlanSubscription.Status.ACTIVE, 2));
    }

    private PlanSubscription subscription(UUID userId, long planId, PlanSubscription.Status status) {
        return PlanSubscription.builder()
                .user(entityManager.getReference(User.class, userId))
                .plan(entityManager.getReference(Plan.class, planId))
                .status(status)
                .startDate(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}
