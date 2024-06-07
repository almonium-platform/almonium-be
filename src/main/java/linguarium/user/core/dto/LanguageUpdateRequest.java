package linguarium.user.core.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import linguarium.engine.translator.model.enums.Language;

public record LanguageUpdateRequest(@NotEmpty List<Language> langCodes) {}
