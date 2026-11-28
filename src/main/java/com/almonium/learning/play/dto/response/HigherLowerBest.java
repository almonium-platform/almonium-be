package com.almonium.learning.play.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;

/** The longest Higher or Lower run a member has played in one language. */
public record HigherLowerBest(Language language, int best) {}
