package com.almonium.user.core.dto.response;

import static lombok.AccessLevel.PRIVATE;

import java.time.Instant;
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
public class BaseUserInfo {
    String id;
    String username;
    String avatarUrl;
    Instant registeredAt;
    boolean isPremium;
    boolean hidden = true;
}
