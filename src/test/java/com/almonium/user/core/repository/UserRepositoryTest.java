package com.almonium.user.core.repository;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.almonium.config.PostgresContainer;
import com.almonium.user.core.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.jdbc.Sql;

@FieldDefaults(level = PRIVATE)
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class UserRepositoryTest {
    private static final String JOHN_EMAIL = "john@email.com";
    private static final String JOHN_USERNAME = "john";
    private static final UUID JOHN_ID = UUID.fromString("01956ce2-34fa-71f2-97b1-dacd41dbaba1");

    @Autowired
    UserRepository userRepository;

    @DisplayName("Should find user by id")
    @Test
    void givenId_whenFindById_thenUserShouldBePresentAndUsernameShouldMatch() {
        Optional<User> user = userRepository.findById(JOHN_ID);
        assertThat(user.orElseThrow().getUsername()).isEqualTo(JOHN_USERNAME);
    }

    @DisplayName("Should find user by email")
    @Test
    void givenEmail_whenFindByEmail_thenUserShouldNotBeNullAndUsernameShouldMatch() {
        Optional<User> user = userRepository.findByEmail(JOHN_EMAIL);
        assertThat(user).isPresent();
        assertThat(user.orElseThrow().getUsername()).isEqualTo(JOHN_USERNAME);
    }

    @DisplayName("Should check if user exists by email")
    @Test
    void givenEmail_whenExistsByEmail_thenUserShouldExist() {
        boolean exists = userRepository.existsByEmail(JOHN_EMAIL);
        assertThat(exists).isTrue();
    }

    @DisplayName("Should check if user exists by username")
    @Test
    void givenUsername_whenExistsByUsername_thenUserShouldExist() {
        boolean exists = userRepository.existsByUsername(JOHN_USERNAME);
        assertThat(exists).isTrue();
    }
}
