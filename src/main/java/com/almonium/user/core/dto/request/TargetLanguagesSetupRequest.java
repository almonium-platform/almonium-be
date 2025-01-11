package com.almonium.user.core.dto.request;

import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TargetLanguagesSetupRequest(@NotEmpty List<TargetLanguageWithProficiency> data) {}
