package com.almonium.learning.almo.dto;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;

/** One Almo channel: a conversation is a language, so there is one of these per target language. */
public record AlmoChatDto(
        String cid, Language language, String name, String placeholder, List<AlmoOpenerDto> openers) {}
