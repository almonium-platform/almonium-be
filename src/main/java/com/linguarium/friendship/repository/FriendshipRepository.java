package com.linguarium.friendship.repository;

import com.linguarium.friendship.model.FriendInfoView;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.FriendshipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipPK> {

    @Query("SELECT f FROM Friendship f WHERE (f.requesterId = :id1 AND f.requesteeId = :id2) OR (f.requesterId = :id2 AND f.requesteeId = :id1)")
    Optional<Friendship> getFriendshipByUsersIds(@Param("id1") long id1, @Param("id2") long id2);

    @Query(value = "SELECT * FROM friend_info_view WHERE user_id = :id", nativeQuery = true)
    List<FriendInfoView> findByUserId(@Param("id") long id);
}
