package com.almonium.infra.stats.service;

import com.almonium.infra.stats.dto.LanguagePulse;
import com.almonium.infra.stats.dto.LanguagePulse.Line;
import com.almonium.infra.stats.dto.LanguagePulse.Miss;
import com.almonium.infra.stats.repository.LanguagePulseStore;
import com.almonium.infra.stats.repository.LanguagePulseStore.LearnerCounts;
import com.almonium.infra.stats.repository.LanguagePulseStore.LookupCounts;
import com.almonium.infra.stats.repository.LanguagePulseStore.MissRow;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Puts learners, Discover lookups and lexicon size side by side per language. A language appears once anyone learns
 * it, looks a word up in it, or the lexicon holds it; the most looked-up come first.
 */
@Service
@RequiredArgsConstructor
public class LanguagePulseService {
    static final int TOP_MISSES = 10;

    private final LanguagePulseStore store;
    private final Clock clock;

    @Transactional(readOnly = true)
    public LanguagePulse report(int days) {
        Instant now = Instant.now(clock);
        Instant today = LocalDate.ofInstant(now, ZoneOffset.UTC)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();
        Instant since = now.minus(Duration.ofDays(days));

        Map<String, LearnerCounts> learners = byLanguage(store.learners(today, since), LearnerCounts::language);
        Map<String, LookupCounts> lookups = byLanguage(store.lookups(today, since), LookupCounts::language);
        Map<String, Long> lemmas = store.lemmas().stream()
                .collect(Collectors.toMap(
                        LanguagePulseStore.LemmaCount::language, LanguagePulseStore.LemmaCount::lemmas));
        Map<String, List<Miss>> misses = store.topMisses(since, TOP_MISSES).stream()
                .collect(Collectors.groupingBy(
                        MissRow::language,
                        Collectors.mapping(
                                row -> new Miss(row.query(), row.lookups(), row.searchers()), Collectors.toList())));

        TreeSet<String> languages = new TreeSet<>(learners.keySet());
        languages.addAll(lookups.keySet());
        languages.addAll(lemmas.keySet());

        List<Line> lines = languages.stream()
                .map(language -> line(
                        language,
                        learners.get(language),
                        lookups.get(language),
                        lemmas.getOrDefault(language, 0L),
                        misses.getOrDefault(language, List.of())))
                .sorted(Comparator.comparingLong(Line::lookups)
                        .thenComparingLong(Line::learners)
                        .reversed()
                        .thenComparing(Line::language))
                .toList();
        return new LanguagePulse(now, days, since, lines);
    }

    private static Line line(
            String language, LearnerCounts learners, LookupCounts lookups, long lemmas, List<Miss> misses) {
        return new Line(
                language,
                learners == null ? 0 : learners.learners(),
                learners == null ? 0 : learners.seenToday(),
                learners == null ? 0 : learners.seen(),
                lookups == null ? 0 : lookups.lookupsToday(),
                lookups == null ? 0 : lookups.lookups(),
                lookups == null ? 0 : lookups.searchers(),
                lookups == null ? 0 : lookups.lexicon(),
                lemmas,
                misses);
    }

    private static <T> Map<String, T> byLanguage(List<T> rows, Function<T, String> language) {
        return rows.stream().collect(Collectors.toMap(language, Function.identity()));
    }
}
