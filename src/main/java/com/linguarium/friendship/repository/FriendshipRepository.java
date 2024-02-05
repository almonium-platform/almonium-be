package com.linguarium.friendship.repository;

import com.linguarium.friendship.model.FriendInfoView;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.FriendshipPK;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipPK> {
    @Query(
            """
            select f from Friendship f
            where (f.requesterId = :id1 and f.requesteeId = :id2)
            or (f.requesterId = :id2 and f.requesteeId = :id1)
            """)
    Optional<Friendship> getFriendshipByUsersIds(@Param("id1") long id1, @Param("id2") long id2);

    @Query(value = "select * from friend_info_view where user_id = :id", nativeQuery = true)
    List<FriendInfoView> findByUserId(@Param("id") long id);
}
