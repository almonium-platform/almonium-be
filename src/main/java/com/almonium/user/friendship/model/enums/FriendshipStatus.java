package com.almonium.user.friendship.model.enums;

import java.util.List;

public enum FriendshipStatus {
    PENDING,
    REJECTED,
    CANCELLED,
    FRIENDS,
    FST_BLOCKED_SND,
    SND_BLOCKED_FST,
    UNFRIENDED;

    public static List<FriendshipStatus> retryableStatuses() {
        return List.of(REJECTED, CANCELLED, UNFRIENDED);
    }

    public boolean isRetryable() {
        return retryableStatuses().contains(this);
    }
}
