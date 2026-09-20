package com.almonium.user.core.dto;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import com.google.firebase.database.annotations.NotNull;

/**
 * A target language with its level and, when the language has several, which variety (design V). The variety is
 * optional on the way in: absent means the language's default, which is what a learner who never touched the row
 * chose.
 */
public record TargetLanguageWithProficiency(
        @NotNull Language language, @NotNull CEFR cefrLevel, LanguageVariety variety) {

    public TargetLanguageWithProficiency(Language language, CEFR cefrLevel) {
        this(language, cefrLevel, null);
    }
}
