package com.linguatool.model.user;

public enum Status {
    FRIENDS("F"), FST_BLOCKED_SND("1B2"), SND_BLOCKED_FST("2B1"), PENDING("P"), REJECTED("R");

    Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private final String code;

    public static boolean isBlocking(Status status) {
        return status != null && (status.equals(Status.FST_BLOCKED_SND) || status.equals(Status.SND_BLOCKED_FST));
    }

}
