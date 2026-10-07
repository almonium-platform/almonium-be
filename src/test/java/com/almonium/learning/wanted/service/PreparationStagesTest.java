package com.almonium.learning.wanted.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.learning.book.dto.response.ProcessorIngestStatus;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PreparationStagesTest {
    static ProcessorIngestStatus status(String phase, UUID published, String... completed) {
        return new ProcessorIngestStatus(
                UUID.randomUUID(), "slug", "processing", phase, 40, "", null, published, List.of(completed), null);
    }

    @Test
    void stagesCountInOrderAndPublishingFinishesThem() {
        assertThat(PreparationStages.done(status("downloading", null))).isZero();
        assertThat(PreparationStages.done(status("ingesting", null, "ingest", "metadata")))
                .isEqualTo(1);
        assertThat(PreparationStages.done(status("ingesting", null, "ingest", "chapter_analysis")))
                .isEqualTo(2);
        assertThat(PreparationStages.done(status("review", null, "ingest", "sentences", "lexical")))
                .isEqualTo(3);
        // A later stage does not count ahead of a missing earlier one.
        assertThat(PreparationStages.done(status("ingesting", null, "lexical"))).isZero();
        assertThat(PreparationStages.done(status("published", null))).isEqualTo(4);
        assertThat(PreparationStages.done(status("ready", UUID.randomUUID()))).isEqualTo(4);
    }
}
