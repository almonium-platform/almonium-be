package com.linguarium.friendship.model;

import com.linguarium.friendship.model.enums.FriendshipStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendInfoView {
    private Long userId;
    private FriendshipStatus status;
    private Boolean isFriendRequester;

    public FriendInfoView(Long userId, String status, Boolean isFriendRequester) {
        this.userId = userId;
        this.status = FriendshipStatus.valueOf(status);
        this.isFriendRequester = isFriendRequester;
    }
}
