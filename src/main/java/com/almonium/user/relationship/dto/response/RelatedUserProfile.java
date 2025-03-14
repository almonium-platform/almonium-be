package com.almonium.user.relationship.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.relationship.model.enums.RelationshipStatus;
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
    RelationshipStatus relationshipStatus;

    public RelatedUserProfile(
            UUID id, String username, String avatarUrl, UUID relationshipId, RelationshipStatus relationshipStatus) {
        super(id, username, avatarUrl);
        this.relationshipId = relationshipId;
        this.relationshipStatus = relationshipStatus;
    }
}
