package com.almonium.learning.rhythm.dto.response;

import java.util.List;

/**
 * @param languages one harness per language the learner studies, in language order
 */
public record RhythmResponse(List<LanguageRhythm> languages) {}
