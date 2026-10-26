package com.almonium.infra.stats.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.almonium.infra.stats.dto.LanguagePulse;
import com.almonium.infra.stats.dto.LanguagePulse.Line;
import com.almonium.infra.stats.dto.LanguagePulse.Miss;
import com.almonium.infra.stats.repository.LanguagePulseStore;
import com.almonium.infra.stats.repository.LanguagePulseStore.LearnerCounts;
import com.almonium.infra.stats.repository.LanguagePulseStore.LemmaCount;
import com.almonium.infra.stats.repository.LanguagePulseStore.LookupCounts;
import com.almonium.infra.stats.repository.LanguagePulseStore.MissRow;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LanguagePulseServiceTest {
    static final Instant NOW = Instant.parse("2026-10-26T12:00:00Z");
    static final Instant TODAY = Instant.parse("2026-10-26T00:00:00Z");
    static final Instant WEEK_AGO = Instant.parse("2026-10-19T12:00:00Z");

    @Mock
    LanguagePulseStore store;

    @Test
    void joinsEveryLanguageAnyoneLearnsLooksUpOrTheLexiconHoldsMostLookedUpFirst() {
        when(store.learners(TODAY, WEEK_AGO))
                .thenReturn(List.of(new LearnerCounts("EN", 5, 1, 3), new LearnerCounts("FR", 2, 0, 0)));
        when(store.lookups(TODAY, WEEK_AGO)).thenReturn(List.of(new LookupCounts("DE", 4, 30, 6, 12)));
        when(store.lemmas()).thenReturn(List.of(new LemmaCount("EN", 900), new LemmaCount("DE", 40)));
        when(store.topMisses(WEEK_AGO, LanguagePulseService.TOP_MISSES))
                .thenReturn(List.of(new MissRow("DE", "kerl", 5, 3), new MissRow("DE", "brot", 2, 2)));

        LanguagePulse pulse = new LanguagePulseService(store, Clock.fixed(NOW, ZoneOffset.UTC)).report(7);

        assertThat(pulse.since()).isEqualTo(WEEK_AGO);
        assertThat(pulse.languages())
                .containsExactly(
                        new Line(
                                "DE",
                                0,
                                0,
                                0,
                                4,
                                30,
                                6,
                                12,
                                40,
                                List.of(new Miss("kerl", 5, 3), new Miss("brot", 2, 2))),
                        new Line("EN", 5, 1, 3, 0, 0, 0, 0, 900, List.of()),
                        new Line("FR", 2, 0, 0, 0, 0, 0, 0, 0, List.of()));
    }
}
