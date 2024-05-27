package linguarium.user.core.dto;

import java.util.Collection;
import linguarium.engine.translator.model.enums.Language;

public record UserInfo(
        String id,
        String username,
        String email,
        Language uiLang,
        String profilePicLink,
        String background,
        Integer streak,
        Collection<Language> targetLangs,
        Collection<Language> fluentLangs,
        Collection<String> tags) {}
