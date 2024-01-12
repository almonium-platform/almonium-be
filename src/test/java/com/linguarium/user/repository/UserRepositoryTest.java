package com.linguarium.user.repository;

import com.linguarium.friendship.model.FriendWrapper;
import com.linguarium.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(scripts = "classpath:db/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class UserRepositoryTest {
    private static final String JOHN_EMAIL = "john@email.com";
    private static final String JOHN_USERNAME = "john";
    private static final Long JOHN_ID = 1L;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find friend by email")
    public void givenEmail_whenFindFriendByEmail_thenFriendShouldBePresentAndUsernameShouldMatch() {
        Optional<FriendWrapper> friend = userRepository.findFriendByEmail(JOHN_EMAIL);
        assertThat(friend).isPresent();
        assertThat(friend.get().getUsername()).isEqualTo(JOHN_USERNAME);
    }

    @Test
    @DisplayName("Should find user by id")
    public void givenId_whenFindById_thenUserShouldBePresentAndUsernameShouldMatch() {
        Optional<User> user = userRepository.findById(JOHN_ID);
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(JOHN_USERNAME);
    }

    @Test
    @DisplayName("Should change username")
    public void givenNewUsernameAndId_whenChangeUsername_thenUserShouldHaveNewUsername() {
        String newUsername = "john_new";
        userRepository.changeUsername(newUsername, JOHN_ID);
        Optional<User> user = userRepository.findById(JOHN_ID);
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(newUsername);
    }

    @Test
    @DisplayName("Should find user by email")
    public void givenEmail_whenFindByEmail_thenUserShouldNotBeNullAndUsernameShouldMatch() {
        User user = userRepository.findByEmail(JOHN_EMAIL);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(JOHN_USERNAME);
    }

    @Test
    @DisplayName("Should check if user exists by email")
    public void givenEmail_whenExistsByEmail_thenUserShouldExist() {
        boolean exists = userRepository.existsByEmail(JOHN_EMAIL);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should check if user exists by username")
    public void givenUsername_whenExistsByUsername_thenUserShouldExist() {
        boolean exists = userRepository.existsByUsername(JOHN_USERNAME);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should find friend by id")
    public void givenId_whenFindAllById_thenFriendShouldBePresentAndUsernameShouldMatch() {
        Optional<FriendWrapper> friend = userRepository.findAllById(JOHN_ID);
        assertThat(friend).isPresent();
        assertThat(friend.orElseThrow().getUsername()).isEqualTo(JOHN_USERNAME);
    }
}
