package com.linguatool.repo;

import com.linguatool.model.user.Friendship;
//import com.linguatool.model.user.FriendshipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query(value = "select * FROM  Friendship where (requester_id = ?1 and requestee_id = ?2) or (requester_id =?2 and requestee_id =?1)", nativeQuery = true)
    Optional<Friendship> getFriendshipByUsersIds(long requesterId, long requesteeId);


    Collection<Friendship> findFriendshipByRequesterId(long requesterId);

    Collection<Friendship> findFriendshipByRequesteeId(long requesteeId);

    default Collection<Friendship> findAllUsersFriendships(long id) {
        Collection<Friendship> friendshipByRequesterId = findFriendshipByRequesterId(id);
        friendshipByRequesterId.addAll(findFriendshipByRequesteeId(id));
        return friendshipByRequesterId;
    }

    default void deleteByUsersIds(Long actionInitiatorId, Long actionRecipientId) {
//        Optional<Friendship> friendship = getFriendshipByUsersIds(actionInitiatorId, actionRecipientId);
//        friendship.ifPresent(value -> this.deleteById(new FriendshipPK(value.getRequester().getId(), value.getRequesteeId())));
    }


}
