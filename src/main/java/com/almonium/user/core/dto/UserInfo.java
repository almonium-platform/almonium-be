package com.almonium.user.core.dto;

import com.almonium.engine.translator.model.enums.Language;
import java.util.Collection;

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
