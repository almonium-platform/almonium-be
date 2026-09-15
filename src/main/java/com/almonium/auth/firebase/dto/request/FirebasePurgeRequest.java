package com.almonium.auth.firebase.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * The project id an operator types to confirm emptying Firebase, and whether their own account goes
 * with it - it is spared unless asked for, since deleting it costs a re-registration and a re-run of
 * the admin-claim script.
 */
public record FirebasePurgeRequest(@NotBlank String confirmation, boolean includeOperator) {}
