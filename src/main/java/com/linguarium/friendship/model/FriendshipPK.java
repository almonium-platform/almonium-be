package com.linguarium.friendship.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"requesterId", "requesteeId"})
public class FriendshipPK implements Serializable {
    private Long requesterId;
    private Long requesteeId;
}