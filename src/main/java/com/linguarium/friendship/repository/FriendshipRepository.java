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

    @Query(
            """
            SELECT new FriendInfoView(u.id, str(f.status), CASE WHEN f.requesterId = :id THEN true ELSE false END)
            FROM User u
            JOIN Friendship f ON (u.id = f.requesterId OR u.id = f.requesteeId)
            WHERE u.id = :id
            """)
    List<FriendInfoView> findByUserId(@Param("id") long id);
}
