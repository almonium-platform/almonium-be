package com.almonium.user.core.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.relationship.model.enums.RelativeRelationshipStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class BaseProfileInfo {
    String id;
    String username;
    String avatarUrl;
    Instant registeredAt;
    boolean isPremium;
    Boolean acceptsRequests;
    RelativeRelationshipStatus relationshipStatus;
    UUID relationshipId;

    @Builder.Default
    final boolean hidden = true;
}
