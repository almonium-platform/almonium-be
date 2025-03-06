package com.almonium.user.friendship.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.friendship.model.enums.FriendshipStatus;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

// do I need it? I can't see friendships of others. There is no admin.
@Data
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FriendshipDto {
    UUID id;
    UUID requesterId;
    UUID requesteeId;
    FriendshipStatus status;
}
