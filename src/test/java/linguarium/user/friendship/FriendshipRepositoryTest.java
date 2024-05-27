package linguarium.user.friendship;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import linguarium.user.friendship.model.Friendship;
import linguarium.user.friendship.model.FriendshipToUserProjection;
import linguarium.user.friendship.repository.FriendshipRepository;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@FieldDefaults(level = PRIVATE)
@Sql(scripts = "classpath:db/add-friendships.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FriendshipRepositoryTest {
    private static final Long REQUESTER_ID = 1L;
    private static final Long REQUESTEE_ID = 2L;
    private static final Long USER_ID = 1L;

    @Autowired
    FriendshipRepository friendshipRepository;

    @DisplayName("Should find friendship by user IDs")
    @Test
    void givenUserIds_whenGetFriendshipByUsersIds_thenFriendshipShouldBePresent() {
        Optional<Friendship> friendship = friendshipRepository.getFriendshipByUsersIds(REQUESTER_ID, REQUESTEE_ID);
        assertThat(friendship).isPresent();
    }

    @DisplayName("Should find friend info by user ID")
    @Test
    void givenUserId_whenGetVisibleFriendships_thenFriendInfoViewShouldBePresent() {
        List<FriendshipToUserProjection> friendshipToUserProjections =
                friendshipRepository.getVisibleFriendships(USER_ID);
        assertThat(friendshipToUserProjections).isNotEmpty();
    }
}
