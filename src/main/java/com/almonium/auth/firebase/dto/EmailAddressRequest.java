package com.almonium.auth.firebase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailAddressRequest(@NotBlank @Email String email) {}
