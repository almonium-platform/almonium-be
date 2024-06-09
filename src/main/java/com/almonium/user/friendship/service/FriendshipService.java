package com.almonium.user.friendship.service;

import com.almonium.user.core.model.entity.User;
import com.almonium.user.friendship.dto.FriendDto;
import com.almonium.user.friendship.dto.FriendshipRequestDto;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.enums.FriendshipAction;
import java.util.List;
import java.util.Optional;

public interface FriendshipService {
    Friendship manageFriendship(User user, Long id, FriendshipAction dto);

    Friendship createFriendshipRequest(User user, FriendshipRequestDto dto);

    Optional<FriendDto> findFriendByEmail(final String email);

    List<FriendDto> getFriends(long id);
}
