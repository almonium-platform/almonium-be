package com.linguarium.friendship.dto;

import com.linguarium.friendship.model.FriendStatus;
import com.linguarium.friendship.model.FriendWrapper;
import com.linguarium.friendship.model.FriendshipStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FriendshipInfoDto {
    FriendStatus status;
    Long id;
    String username;
    String email;

    public FriendshipInfoDto(FriendWrapper friendWrapper) {
        initCommonFields(friendWrapper);
    }

    public FriendshipInfoDto(FriendWrapper friendWrapper, FriendshipStatus status, boolean isFriendRequester) {
        initCommonFields(friendWrapper);
        this.status = pickStatus(status, isFriendRequester);
    }

    private void initCommonFields(FriendWrapper friendWrapper) {
        this.id = friendWrapper.getId();
        this.email = friendWrapper.getEmail();
        this.username = friendWrapper.getUsername();
    }

    private FriendStatus pickStatus(FriendshipStatus status, boolean isFriendRequester) {
        return switch (status) {
            case FRIENDS -> FriendStatus.FRIENDS;
            case PENDING -> isFriendRequester ? FriendStatus.ASKED_ME : FriendStatus.ASKED;
            case FST_BLOCKED_SND -> isFriendRequester ? FriendStatus.BLOCKED_ME : FriendStatus.BLOCKED;
            case SND_BLOCKED_FST -> isFriendRequester ? FriendStatus.BLOCKED : FriendStatus.BLOCKED_ME;
            default -> null;
        };
    }
}
