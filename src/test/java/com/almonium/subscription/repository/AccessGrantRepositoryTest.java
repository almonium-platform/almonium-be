package com.almonium.subscription.repository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.config.PostgresContainer;
import com.almonium.subscription.model.entity.AccessGrant;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@FieldDefaults(level = PRIVATE)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AccessGrantRepositoryTest {
    private static final UUID USER_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");
    private static final UUID OPERATOR_ID = UUID.fromString("01956cde-a541-7ac1-8b32-2896d096ecdf");

    @Autowired
    AccessGrantRepository accessGrantRepository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("Should revoke the active grant and leave none active")
    @Test
    void givenActiveGrant_whenRevokeActiveByUserId_thenNoneRemainActive() {
        accessGrantRepository.saveAndFlush(newGrant(Entitlement.PREMIUM));

        int updated = accessGrantRepository.revokeActiveByUserId(USER_ID, Instant.now());

        assertThat(updated).isEqualTo(1);
        assertThat(accessGrantRepository.findActiveByUserId(USER_ID, Instant.now()))
                .isEmpty();
    }

    @DisplayName("Should reject a second active grant for the same user")
    @Test
    void givenActiveGrant_whenInsertingAnotherActiveGrant_thenConstraintRejectsIt() {
        accessGrantRepository.saveAndFlush(newGrant(Entitlement.PREMIUM));

        AccessGrant second = newGrant(Entitlement.UNLIMITED);
        assertThatThrownBy(() -> accessGrantRepository.saveAndFlush(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("Should allow a new active grant after the previous one was revoked")
    @Test
    void givenRevokedGrant_whenInsertingNewActiveGrant_thenItSucceeds() {
        accessGrantRepository.saveAndFlush(newGrant(Entitlement.PREMIUM));
        accessGrantRepository.revokeActiveByUserId(USER_ID, Instant.now());

        AccessGrant replacement = newGrant(Entitlement.UNLIMITED);
        accessGrantRepository.saveAndFlush(replacement);

        assertThat(accessGrantRepository.findActiveByUserId(USER_ID, Instant.now()))
                .contains(replacement);
    }

    private AccessGrant newGrant(Entitlement entitlement) {
        AccessGrant grant = new AccessGrant();
        grant.setUser(entityManager.getReference(User.class, USER_ID));
        grant.setGrantedBy(entityManager.getReference(User.class, OPERATOR_ID));
        grant.setEntitlement(entitlement);
        grant.setReason("test");
        grant.setStartsAt(Instant.now());
        return grant;
    }
}
