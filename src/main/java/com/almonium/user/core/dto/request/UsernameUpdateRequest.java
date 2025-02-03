package com.almonium.user.core.dto.request;

import com.almonium.subscription.constant.AppLimits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsernameUpdateRequest(
        @NotBlank @Size(min = AppLimits.MIN_USERNAME_LENGTH, max = AppLimits.MAX_USERNAME_LENGTH) String username) {}
