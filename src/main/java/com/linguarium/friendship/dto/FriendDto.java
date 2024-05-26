package com.linguarium.friendship.dto;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.friendship.model.UserToFriendProjection;
import com.linguarium.friendship.model.enums.FriendStatus;
import com.linguarium.friendship.model.enums.FriendshipStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Data Transfer Object (DTO) for friend information.
 *
 * <p>This class is used to transfer friend data to the frontend.
 * It includes the friend's ID, username, email, and the status of the friendship.</p>
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FriendDto {
    long id;
    String username;
    String email;
    FriendStatus status;

    public FriendDto(UserToFriendProjection userToFriendProjection) {
        initCommonFields(userToFriendProjection);
    }

    public FriendDto(
            UserToFriendProjection userToFriendProjection, FriendshipStatus status, boolean isFriendRequester) {
        initCommonFields(userToFriendProjection);
        this.status = pickStatus(status, isFriendRequester);
    }

    private void initCommonFields(UserToFriendProjection userToFriendProjection) {
        id = userToFriendProjection.getId();
        email = userToFriendProjection.getEmail();
        username = userToFriendProjection.getUsername();
    }

    // here we will have already filtered friendships - blocked by us
    private FriendStatus pickStatus(FriendshipStatus status, boolean isFriendRequester) {
        return switch (status) {
            case FRIENDS -> FriendStatus.FRIENDS;
            case PENDING -> isFriendRequester ? FriendStatus.ASKED_ME : FriendStatus.ASKED_THEM;
            case MUTUALLY_BLOCKED, FST_BLOCKED_SND, SND_BLOCKED_FST -> FriendStatus.BLOCKED;
            default -> throw new IllegalArgumentException("Invalid friendship status: " + status);
        };
    }
}
