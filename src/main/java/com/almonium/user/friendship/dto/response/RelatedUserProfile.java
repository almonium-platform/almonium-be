package com.almonium.user.friendship.dto.response;

import static lombok.AccessLevel.PRIVATE;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
public class RelatedUserProfile extends PublicUserProfile {
    long friendshipId;

    public RelatedUserProfile(long id, String username, String avatarUrl, long friendshipId) {
        super(id, username, avatarUrl);
        this.friendshipId = friendshipId;
    }
}
