package com.almonium.user.friendship;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.config.PostgresContainer;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.projection.FriendshipToUserProjection;
import com.almonium.user.friendship.repository.FriendshipRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@FieldDefaults(level = PRIVATE)
@Sql(scripts = "classpath:db/add-friendships.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FriendshipRepositoryTest {
    private static final UUID REQUESTER_ID = UUID.fromString("01956cde-a541-7ac1-8b32-2896d096ecdf");
    private static final UUID REQUESTEE_ID = UUID.fromString("01956cde-d6dd-7aca-bd07-e5c29cadf093");

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
                friendshipRepository.getVisibleFriendships(REQUESTER_ID);
        assertThat(friendshipToUserProjections).isNotEmpty();
    }
}
