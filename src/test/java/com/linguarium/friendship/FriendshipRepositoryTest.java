package com.linguarium.friendship;

import static org.assertj.core.api.Assertions.assertThat;

import com.linguarium.friendship.model.FriendInfoView;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.repository.FriendshipRepository;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Sql(scripts = "classpath:db/add-friendships.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FriendshipRepositoryTest {

    @Autowired
    FriendshipRepository friendshipRepository;

    private static final Long REQUESTER_ID = 1L;
    private static final Long REQUESTEE_ID = 2L;
    private static final Long USER_ID = 1L;

    @DisplayName("Should find friendship by user IDs")
    @Test
    void givenUserIds_whenGetFriendshipByUsersIds_thenFriendshipShouldBePresent() {
        Optional<Friendship> friendship = friendshipRepository.getFriendshipByUsersIds(REQUESTER_ID, REQUESTEE_ID);
        assertThat(friendship).isPresent();
    }

    @DisplayName("Should find friend info by user ID")
    @Test
    void givenUserId_whenFindByUserId_thenFriendInfoViewShouldBePresent() {
        List<FriendInfoView> friendInfoViews = friendshipRepository.findByUserId(USER_ID);
        assertThat(friendInfoViews).isNotEmpty();
    }
}
