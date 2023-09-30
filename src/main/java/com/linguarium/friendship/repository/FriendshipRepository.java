package com.linguarium.friendship.repository;

import com.linguarium.friendship.model.FriendInfoView;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.FriendshipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipPK> {

    @Query("SELECT f FROM Friendship f WHERE (f.requesterId = :user1Id AND f.requesteeId = :user2Id) OR (f.requesterId = :user2Id AND f.requesteeId = :user1Id)")
    Optional<Friendship> getFriendshipByUsersIds(@Param("user1Id") long user1Id, @Param("user2Id") long user2Id);

    @Query(value = "SELECT * FROM friend_info_view WHERE user_id = :userId", nativeQuery = true)
    List<FriendInfoView> findByUserId(@Param("userId") Long userId);
}
