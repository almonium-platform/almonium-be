package com.linguarium.friendship.service;

import com.linguarium.friendship.dto.FriendshipInfoDto;
import com.linguarium.friendship.dto.FriendshipRequestDto;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.FriendshipAction;
import com.linguarium.user.model.User;
import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship manageFriendship(User user, Long id, FriendshipAction dto);

    Friendship createFriendshipRequest(User user, FriendshipRequestDto dto);

    Optional<FriendshipInfoDto> findFriendByEmail(final String email);

    List<FriendshipInfoDto> getFriendships(long id);
}
