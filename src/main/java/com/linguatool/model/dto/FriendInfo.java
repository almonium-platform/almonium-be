package com.linguatool.model.dto;

import com.linguatool.model.entity.user.FriendStatus;
import com.linguatool.model.entity.user.Friendship;
import com.linguatool.model.entity.user.FriendshipStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendInfo {
    FriendStatus status;
    Long id;
    String username;
    String email;


    public FriendInfo(Friend friend, FriendStatus status) {
        this.status = status;
        this.id = friend.getId();
        this.email = friend.getEmail();
        this.username = friend.getUsername();
    }

    public FriendInfo(Friend friend) {
        this.status = null;
        this.id = friend.getId();
        this.email = friend.getEmail();
        this.username = friend.getUsername();
    }

    public FriendInfo(Friend friend, Friendship friendship) {
        this.status = pickStatus(friendship);
        this.id = friend.getId();
        this.email = friend.getEmail();
        this.username = friend.getUsername();
    }

    public FriendInfo(Friend friend, FriendshipStatus status, boolean isFriendRequester) {
        this.status = pickStatus(status, isFriendRequester);
        this.id = friend.getId();
        this.email = friend.getEmail();
        this.username = friend.getUsername();
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

    private FriendStatus pickStatus(Friendship friendship) {
        FriendshipStatus friendshipStatus = friendship.getFriendshipStatus();
        switch (friendshipStatus) {
            case FRIENDS:
                return FriendStatus.FRIENDS;
            case PENDING: {
                if (friendship.getRequesterId().equals(id)) {
                    return FriendStatus.ASKED_ME;
                } else {
                    return FriendStatus.ASKED;
                }
            }
            case FST_BLOCKED_SND: {
                if (friendship.getRequesterId().equals(id)) {
                    return FriendStatus.BLOCKED_ME;
                } else {
                    return FriendStatus.BLOCKED;
                }
            }
            case SND_BLOCKED_FST: {
                if (friendship.getRequesterId().equals(id)) {
                    return FriendStatus.BLOCKED;
                } else {
                    return FriendStatus.BLOCKED_ME;
                }
            }
        }
        return null;
    }

}
