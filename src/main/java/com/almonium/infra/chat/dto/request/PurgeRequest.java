package com.almonium.infra.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

/** The phrase an operator types to confirm emptying the Stream application. */
public record PurgeRequest(@NotBlank String confirmation) {}
