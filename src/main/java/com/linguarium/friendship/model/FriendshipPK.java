package com.linguarium.friendship.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
