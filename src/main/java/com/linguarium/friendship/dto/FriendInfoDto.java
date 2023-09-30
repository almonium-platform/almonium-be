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
public class FriendInfoDto {
    FriendStatus status;
    Long id;
    String username;
    String email;

    public FriendInfoDto(FriendWrapper friendWrapper) {
        initCommonFields(friendWrapper);
    }

    public FriendInfoDto(FriendWrapper friendWrapper, FriendshipStatus status, boolean isFriendRequester) {
        initCommonFields(friendWrapper);
        this.status = pickStatus(status, isFriendRequester);
    }

    private void initCommonFields(FriendWrapper friendWrapper) {
        this.id = friendWrapper.getId();
        this.email = friendWrapper.getEmail();
        this.username = friendWrapper.getUsername();
    }

    private FriendStatus pickStatus(FriendshipStatus status, boolean isFriendRequester) {
        switch (status) {
            case FRIENDS:
                return FriendStatus.FRIENDS;
            case PENDING: {
                return isFriendRequester ? FriendStatus.ASKED_ME : FriendStatus.ASKED;
            }
            case FST_BLOCKED_SND: {
                return isFriendRequester ? FriendStatus.BLOCKED_ME : FriendStatus.BLOCKED;
            }
            case SND_BLOCKED_FST: {
                return isFriendRequester ? FriendStatus.BLOCKED : FriendStatus.BLOCKED_ME;
            }
        }
        return null;
    }
}
