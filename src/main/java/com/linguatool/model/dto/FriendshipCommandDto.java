package com.linguatool.model.dto;

import com.linguatool.model.entity.user.FriendshipAction;
import lombok.Data;

@Data
public class FriendshipCommandDto {

    Long idInitiator;
    Long idAcceptor;
    FriendshipAction action;
}
