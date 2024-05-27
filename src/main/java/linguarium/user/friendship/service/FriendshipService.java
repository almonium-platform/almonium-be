package linguarium.user.friendship.service;

import java.util.List;
import java.util.Optional;
import linguarium.user.core.model.User;
import linguarium.user.friendship.dto.FriendDto;
import linguarium.user.friendship.dto.FriendshipRequestDto;
import linguarium.user.friendship.model.Friendship;
import linguarium.user.friendship.model.enums.FriendshipAction;

public interface FriendshipService {
    Friendship manageFriendship(User user, Long id, FriendshipAction dto);

    Friendship createFriendshipRequest(User user, FriendshipRequestDto dto);

    Optional<FriendDto> findFriendByEmail(final String email);

    List<FriendDto> getFriends(long id);
}
