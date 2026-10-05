package com.almonium.learning.rights.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/** /ops naming the catalogue work a shelf work is an edition of, whose facts its copyright verdict then uses. */
public record CatalogLinkRequest(@NotNull UUID catalogWorkId) {}
