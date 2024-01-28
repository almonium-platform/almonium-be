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
public class FriendshipActionDto {
    private Long idInitiator;
    private Long idAcceptor;
    private FriendshipAction action;
}
