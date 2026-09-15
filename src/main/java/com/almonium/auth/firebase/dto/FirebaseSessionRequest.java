package com.almonium.auth.firebase.dto;

import jakarta.validation.constraints.NotBlank;

public record FirebaseSessionRequest(@NotBlank String idToken) {}
