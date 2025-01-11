package com.almonium.user.core.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SaveInterestsRequest(List<@NotNull Long> ids) {}
