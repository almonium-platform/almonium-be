package com.linguarium.user.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LanguageUpdateRequest(@NotEmpty List<String> langCodes) {}
