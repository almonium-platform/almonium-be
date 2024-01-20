package com.linguarium.friendship.service;

import com.linguarium.friendship.dto.FriendInfoDto;
import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.model.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship manageFriendship(FriendshipActionDto dto);

    Optional<FriendInfoDto> findFriendByEmail(final String email);

    List<FriendInfoDto> getFriends(long id);
}
