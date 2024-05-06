package com.linguarium.friendship.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friend_info_view")
public class FriendInfoView {
    @Id
    private Long userId;

    private String status; // TODO change to FriendshipStatus
    private Boolean isFriendRequester;
}
