package com.linguarium.friendship.service;

import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.dto.FriendInfoDto;
import com.linguarium.friendship.model.Friendship;

import java.util.Collection;
import java.util.Optional;

public interface FriendshipService {
    Friendship editFriendship(FriendshipActionDto dto);

    Optional<FriendInfoDto> findFriendByEmail(final String email);

    Collection<FriendInfoDto> getFriends(long id) ;
}
