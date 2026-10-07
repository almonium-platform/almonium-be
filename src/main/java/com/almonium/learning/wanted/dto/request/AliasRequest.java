package com.almonium.learning.wanted.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/** /ops teaching search that a query that found nothing means this work. */
public record AliasRequest(
        @NotBlank @Size(max = 200) String query, @NotNull UUID catalogWorkId) {}
