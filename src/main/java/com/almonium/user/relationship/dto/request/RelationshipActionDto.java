package com.almonium.user.relationship.dto.request;

import com.almonium.user.relationship.model.enums.RelationshipAction;
import jakarta.validation.constraints.NotNull;

public record RelationshipActionDto(@NotNull RelationshipAction action) {}
