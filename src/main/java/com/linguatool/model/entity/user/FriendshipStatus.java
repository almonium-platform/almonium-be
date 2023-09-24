package com.linguatool.model.entity.user;

public enum FriendshipStatus {
    FRIENDS("F"),
    FST_BLOCKED_SND("1B2"),
    SND_BLOCKED_FST("2B1"),
    PENDING("P"),
    REJECTED("R");

    FriendshipStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private final String code;

    public static FriendshipStatus fromString(String text) {
        for (FriendshipStatus b : FriendshipStatus.values()) {
            if (b.code.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }


    public static boolean isBlocking(FriendshipStatus friendshipStatus) {
        return friendshipStatus != null && (friendshipStatus.equals(FriendshipStatus.FST_BLOCKED_SND) || friendshipStatus.equals(FriendshipStatus.SND_BLOCKED_FST));
    }

}
