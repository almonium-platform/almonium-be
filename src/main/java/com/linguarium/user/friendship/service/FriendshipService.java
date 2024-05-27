package com.linguarium.user.friendship.service;

import com.linguarium.user.core.model.User;
import com.linguarium.user.friendship.dto.FriendDto;
import com.linguarium.user.friendship.dto.FriendshipRequestDto;
import com.linguarium.user.friendship.model.Friendship;
import com.linguarium.user.friendship.model.enums.FriendshipAction;
import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship manageFriendship(User user, Long id, FriendshipAction dto);

    Friendship createFriendshipRequest(User user, FriendshipRequestDto dto);

    Optional<FriendDto> findFriendByEmail(final String email);

    List<FriendDto> getFriends(long id);
}
