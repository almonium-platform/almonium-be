package com.almonium.user.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record AvatarUrlDto(@NotBlank @URL String avatarUrl) {}
