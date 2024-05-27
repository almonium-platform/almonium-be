package com.linguarium.user.friendship.model;

import com.linguarium.user.friendship.model.enums.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A projection representing information about a user's friendships.
 *
 * <p>This class includes the user's ID, the status of the friendship,
 * and a boolean indicating whether the user is the requester of the friendship.</p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipToUserProjection {
    private long userId;
    private FriendshipStatus status;
    private boolean isRequester;

    public FriendshipToUserProjection(long userId, String status, boolean isRequester) {
        this.userId = userId;
        this.status = FriendshipStatus.valueOf(status);
        this.isRequester = isRequester;
    }
}
