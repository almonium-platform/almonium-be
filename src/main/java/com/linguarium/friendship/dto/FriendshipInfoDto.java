package com.linguarium.friendship.dto;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.friendship.model.FriendProjection;
import com.linguarium.friendship.model.enums.FriendStatus;
import com.linguarium.friendship.model.enums.FriendshipStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FriendshipInfoDto {
    Long id;
    String username;
    String email;
    FriendStatus status;

    public FriendshipInfoDto(FriendProjection friendProjection) {
        initCommonFields(friendProjection);
    }

    public FriendshipInfoDto(FriendProjection friendProjection, FriendshipStatus status, boolean isFriendRequester) {
        initCommonFields(friendProjection);
        this.status = pickStatus(status, isFriendRequester);
    }

    private void initCommonFields(FriendProjection friendProjection) {
        id = friendProjection.getId();
        email = friendProjection.getEmail();
        username = friendProjection.getUsername();
    }

    // here we will have already filtered friendships - blocked by us
    private FriendStatus pickStatus(FriendshipStatus status, boolean isFriendRequester) {
        return switch (status) {
            case FRIENDS -> FriendStatus.FRIENDS;
            case PENDING -> isFriendRequester ? FriendStatus.ASKED_ME : FriendStatus.ASKED_THEM;
            case MUTUALLY_BLOCKED, FST_BLOCKED_SND, SND_BLOCKED_FST -> FriendStatus.BLOCKED;
            default -> throw new IllegalArgumentException("Invalid friendship status");
        };
    }
}
