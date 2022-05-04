package com.linguatool.model.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FriendshipPK implements Serializable {

    private Long requesterId;

    private Long requesteeId;
}
