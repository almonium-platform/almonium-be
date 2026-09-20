package com.almonium.user.core.dto.request;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.LanguageVariety;

/** Each field is a separate change and null leaves it alone; the variety must be one of the learner's language. */
public record UpdateLearnerRequest(Boolean active, CEFR level, LanguageVariety variety) {

    public UpdateLearnerRequest(Boolean active, CEFR level) {
        this(active, level, null);
    }
}
