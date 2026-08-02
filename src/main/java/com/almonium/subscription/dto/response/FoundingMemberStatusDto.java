package com.almonium.subscription.dto.response;

public record FoundingMemberStatusDto(int capacity, long claimed) {
    public long remaining() {
        return capacity - claimed;
    }
}
