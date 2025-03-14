package com.almonium.user.relationship.model.projection;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.relationship.model.enums.RelationshipStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * A projection representing information about a user's friendships.
 *
 * <p>This class includes the user's ID, the status of the friendship,
 * and a boolean indicating whether the user is the requester of the friendship.</p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class RelationshipToUserProjection {
    UUID userId;
    RelationshipStatus status;
    boolean isRequester;

    public RelationshipToUserProjection(UUID userId, String status, boolean isRequester) {
        this.userId = userId;
        this.isRequester = isRequester;
        this.status = RelationshipStatus.valueOf(status);
    }
}
