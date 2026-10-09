package com.almonium.learning.wanted.service;

import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import java.util.List;
import java.util.Set;

/**
 * The processor's pipeline as a reader sees it on the Wanted page (W3): four stages, text, chapters, level and
 * words, on the shelf. A stage is done when the processor reports any of its pipeline stages succeeded; the stages
 * count as done only in order, so a later one never shows ahead of an earlier one.
 */
public final class PreparationStages {
    static final List<Set<String>> STAGES = List.of(
            Set.of("ingest", "metadata", "source_qa"),
            Set.of("chapter_analysis", "sentences", "align"),
            Set.of("lexical", "glosses", "adapt", "translate"),
            Set.of("publish", "promote"));

    private PreparationStages() {}

    /** How many of the four stages are done, in order. */
    public static int done(ProcessorIngestStatus status) {
        if ("published".equals(status.phase()) || status.publishedBookId() != null) {
            return STAGES.size();
        }
        int done = 0;
        for (Set<String> stage : STAGES) {
            if (status.completedStages().stream().noneMatch(stage::contains)) {
                break;
            }
            done++;
        }
        return done;
    }
}
