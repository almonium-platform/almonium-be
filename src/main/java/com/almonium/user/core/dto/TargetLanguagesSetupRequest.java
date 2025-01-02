package com.almonium.user.core.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TargetLanguagesSetupRequest(@NotEmpty List<TargetLanguageWithProficiency> data) {}
