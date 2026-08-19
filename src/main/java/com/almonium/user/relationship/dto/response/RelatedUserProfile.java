package com.almonium.user.relationship.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.relationship.model.enums.RelativeRelationshipStatus;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = PRIVATE)
public class RelatedUserProfile extends PublicUserProfile {
    UUID relationshipId;
    RelativeRelationshipStatus relationshipStatus;

    public RelatedUserProfile(
            UUID id,
            String username,
            String avatarUrl,
            boolean premium,
            UUID relationshipId,
            RelativeRelationshipStatus relationshipStatus) {
        super(id, username, avatarUrl, premium);
        this.relationshipId = relationshipId;
        this.relationshipStatus = relationshipStatus;
    }

    public RelatedUserProfile(
            UUID id,
            String username,
            String avatarUrl,
            boolean premium,
            UUID relationshipId,
            String relationshipStatus) {
        this(id, username, avatarUrl, premium, relationshipId, RelativeRelationshipStatus.valueOf(relationshipStatus));
    }
}
