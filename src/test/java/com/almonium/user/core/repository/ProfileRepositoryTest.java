package com.almonium.user.core.repository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.config.PostgresContainer;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.jdbc.Sql;

@FieldDefaults(level = PRIVATE)
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProfileRepositoryTest {
    private static final UUID JOHN_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void stampsLastSeenByIdAndCountsWhoWasSeenSince() {
        User john = userRepository.findById(JOHN_ID).orElseThrow();
        profileRepository.saveAndFlush(
                Profile.builder().user(john).lastLogin(LocalDateTime.now()).build());
        Instant seenAt = Instant.now().truncatedTo(ChronoUnit.MICROS);

        int stamped = profileRepository.stampLastSeen(JOHN_ID, seenAt);

        assertThat(stamped).isEqualTo(1);
        assertThat(profileRepository.findById(JOHN_ID).orElseThrow().getLastSeenAt())
                .isEqualTo(seenAt);
        assertThat(profileRepository.countByLastSeenAtGreaterThanEqual(seenAt.minusSeconds(60)))
                .isEqualTo(1);
        assertThat(profileRepository.countByLastSeenAtGreaterThanEqual(seenAt.plusSeconds(60)))
                .isZero();
    }

    @Test
    void stampingAnUnknownUserTouchesNothing() {
        assertThat(profileRepository.stampLastSeen(UUID.randomUUID(), Instant.now()))
                .isZero();
    }
}
