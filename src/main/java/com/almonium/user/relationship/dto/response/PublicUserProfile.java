package com.almonium.user.relationship.dto.response;

import static lombok.AccessLevel.PRIVATE;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor
public class PublicUserProfile {
    UUID id;
    String username;
    String avatarUrl;
}
