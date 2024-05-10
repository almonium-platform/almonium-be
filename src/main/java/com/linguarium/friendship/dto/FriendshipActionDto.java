package com.linguarium.friendship.dto;

import com.linguarium.friendship.model.FriendshipAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipActionDto { // todo try record
    private Long initiatorId;
    private Long recipientId;
    private FriendshipAction action;
}
