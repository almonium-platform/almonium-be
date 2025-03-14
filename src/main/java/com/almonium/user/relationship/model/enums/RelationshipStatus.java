package com.almonium.user.relationship.model.enums;

import java.util.List;

public enum RelationshipStatus {
    PENDING,
    REJECTED,
    CANCELLED,
    FRIENDS,
    FST_BLOCKED_SND,
    SND_BLOCKED_FST,
    MUTUAL_BLOCK,
    UNFRIENDED;

    public static List<RelationshipStatus> retryableStatuses() {
        return List.of(REJECTED, CANCELLED, UNFRIENDED);
    }

    public boolean isRetryable() {
        return retryableStatuses().contains(this);
    }
}
