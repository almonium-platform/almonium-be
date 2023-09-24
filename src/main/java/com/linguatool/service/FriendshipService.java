package com.linguatool.service;

import com.linguatool.model.dto.FriendInfo;
import com.linguatool.model.dto.FriendshipCommandDto;
import com.linguatool.model.entity.user.Friendship;

import java.util.Collection;
import java.util.Optional;

public interface FriendshipService {
    void editFriendship(FriendshipCommandDto dto);

    Optional<FriendInfo> findFriendByEmail(final String email);

    Collection<FriendInfo> getUsersFriends(long id) ;

    Friendship blockUser(long actionInitiatorId, long actionAcceptorId) ;

    Friendship cancelFriendship(Long actionInitiatorId, Long actionAcceptorId);
}
