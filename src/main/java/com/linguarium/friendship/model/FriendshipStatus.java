package com.linguarium.friendship.model;

import lombok.Getter;

@Getter
public enum FriendshipStatus {
    FRIENDS("F"),
    MUTUALLY_BLOCKED("MB"),
    FST_BLOCKED_SND("1B2"),
    SND_BLOCKED_FST("2B1"),
    PENDING("P"),
    REJECTED("R");

    private final String code;

    FriendshipStatus(String code) {
        this.code = code;
    }

    public static FriendshipStatus fromString(String text) {
        for (FriendshipStatus status : FriendshipStatus.values()) {
            if (status.code.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Couldn't find friendship status: " + text);
    }
}
