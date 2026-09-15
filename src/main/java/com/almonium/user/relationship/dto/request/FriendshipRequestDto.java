package com.almonium.user.relationship.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FriendshipRequestDto(@NotNull UUID recipientId) {}
