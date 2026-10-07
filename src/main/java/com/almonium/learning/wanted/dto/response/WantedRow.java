package com.almonium.learning.wanted.dto.response;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.wanted.model.enums.WantStatus;
import java.util.UUID;

/**
 * One row of the public queue (W3): a work in a language, its rank by distinct readers, and whether the viewer wants
 * it. {@code workSlug} is set when the shelf already has the work in another language, so the row reads as "Polish
 * edition". {@code readers} is null below three. {@code stagesDone} counts, for a work being prepared from a free text,
 * how many of the four stages are done (text, chapters, level and words, on the shelf); null otherwise.
 */
public record WantedRow(
        UUID catalogWorkId,
        String title,
        String author,
        Language language,
        String workSlug,
        int rank,
        Integer readers,
        boolean wantedByMe,
        WantStatus status,
        Integer stagesDone) {}
