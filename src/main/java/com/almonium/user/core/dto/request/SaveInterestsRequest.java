package com.almonium.user.core.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record SaveInterestsRequest(Set<@NotNull Long> ids) {}
