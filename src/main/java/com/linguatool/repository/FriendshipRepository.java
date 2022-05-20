package com.linguatool.repository;

import com.linguatool.model.user.Friendship;
//import com.linguatool.model.user.FriendshipPK;
import com.linguatool.model.user.FriendshipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipPK> {

    @Query(value = " select * from Friendship" +
        "            where (requester_id = ?1 and requestee_id = ?2)" +
        "            or (requester_id =?2 and requestee_id =?1)     ", nativeQuery = true)
    Optional<Friendship> getFriendshipByUsersIds(long user1Id, long user2Id);


    @Query(value = "(select friendship.requestee_id, friendship.status, false from Friendship where requester_id = ?1) union" +
        "           (select friendship.requester_id, friendship.status, true from Friendship where requestee_id = ?1)       ", nativeQuery = true)
    List<Object[]> getUsersFriendsIdsAndStatuses(long userId);

    @Query(value = "(select friendship from Friendship where requester_id = ?1) union " +
        "           (select friendship from Friendship where requestee_id = ?1)       ", nativeQuery = true)
    Collection<Friendship> getUsersFriendships(long userId);
    @Modifying
    @Transactional
    @Query(value = "delete from friendship                          " +
        "           where (requester_id = ?1 and requestee_id = ?2) " +
        "           or (requester_id = ?2 and requestee_id = ?1)    ", nativeQuery = true)
    void deleteFriendshipByIds(long id1, long id2);


}
