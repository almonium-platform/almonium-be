package com.linguarium.friendship.service;

import com.linguarium.friendship.dto.FriendshipInfoDto;
import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.model.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship manageFriendship(FriendshipActionDto dto);

    Optional<FriendshipInfoDto> findFriendByEmail(final String email);

    List<FriendshipInfoDto> getFriends(long id);
}
