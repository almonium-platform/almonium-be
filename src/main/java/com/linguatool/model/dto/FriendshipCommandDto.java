package com.linguatool.model.dto;

import com.linguatool.model.user.FriendshipAction;
import lombok.Data;

@Data
public class FriendshipCommandDto {

    Long idInitiator;
    Long idAcceptor;
    FriendshipAction action;
}
