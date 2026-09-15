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

    /**
     * The shape a query builds. Membership is left false here on purpose: only EffectiveAccessService knows whether a
     * person is a member, so the service fills it in rather than every query re-deriving it from plan rows.
     */
    public RelatedUserProfile(
            UUID id, String username, String avatarUrl, UUID relationshipId, String relationshipStatus) {
        this(id, username, avatarUrl, false, relationshipId, relationshipStatus);
    }
}
