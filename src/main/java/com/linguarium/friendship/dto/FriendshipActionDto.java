package com.linguarium.friendship.dto;

import com.linguarium.friendship.model.FriendshipAction;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NotEqual(firstField = "idInitiator", secondField = "idAcceptor", message = "idInitiator and idAcceptor must not be the same")
public class FriendshipActionDto {
    Long idInitiator;
    Long idAcceptor;
    FriendshipAction action;
}
