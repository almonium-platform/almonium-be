package com.almonium.learning.rhythm.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.rhythm.dto.request.LearningActivityRequest;
import com.almonium.learning.rhythm.dto.response.LanguageRhythm;
import com.almonium.learning.rhythm.dto.response.PaceSnapshot;
import com.almonium.learning.rhythm.dto.response.RhythmDay;
import com.almonium.learning.rhythm.dto.response.RhythmResponse;
import com.almonium.learning.rhythm.dto.response.RhythmWeek;
import com.almonium.learning.rhythm.model.LearningDay;
import com.almonium.learning.rhythm.repository.LearningDayRepository;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.repository.LearnerRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The harness: whether the learner cleared the bar they set for themselves, week by week, in one language.
 *
 * <p>Weeks are the unit, so a missed Tuesday is not a failure and nothing can be lost. A day counts as met on any
 * completed learning event, whatever it was; the accumulated seconds are texture for the band's tint only. The bar
 * belongs to a language rather than to the account, so the band says which commitment was kept.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class LearningRhythmService {
    /** Weeks in the band: thirteen finished weeks plus the one in progress. */
    static final int BAND_WEEKS = 14;

    /** Weeks every surface talks about; the extra band weeks are slack for partial-week edges. */
    static final int WEEKS_SHOWN = 12;

    /** A single report cannot claim more than this, however long a tab was left open. */
    static final int MAX_SECONDS_PER_REPORT = 600;

    /** Spent across the whole account, so several languages do not buy several days' worth of hours. */
    static final int MAX_SECONDS_PER_DAY = 12 * 60 * 60;

    LearningDayRepository learningDayRepository;
    LearnerRepository learnerRepository;

    @Transactional
    public void recordActivity(UUID userId, LearningActivityRequest request) {
        int seconds = Math.clamp(request.seconds(), 0, MAX_SECONDS_PER_REPORT);
        boolean met = request.completed() || seconds > 0;
        if (!met) {
            return;
        }
        // A set-aside language stops accruing: due counts are facts about the deck, learning days are judgements
        // about the person, and nothing is asked of a language with no editable target.
        if (!requireLearner(userId, request.language()).isActive()) {
            return;
        }
        learningDayRepository.accumulate(
                UuidCreator.getTimeOrderedEpoch(),
                userId,
                resolveLocalDate(request.localDate()),
                request.language().name(),
                seconds,
                true,
                MAX_SECONDS_PER_DAY);
    }

    public RhythmResponse getRhythm(UUID userId, LocalDate today) {
        LocalDate currentWeekStart = resolveLocalDate(today).with(DayOfWeek.MONDAY);
        LocalDate from = currentWeekStart.minusWeeks(BAND_WEEKS - 1L);
        LocalDate to = currentWeekStart.plusDays(6);

        Map<Language, Map<LocalDate, LearningDay>> activity =
                learningDayRepository.findAllByUserIdAndDayBetweenOrderByDayAsc(userId, from, to).stream()
                        .collect(groupingBy(LearningDay::getLanguage, toMap(LearningDay::getDay, Function.identity())));

        Map<Language, LocalDate> firstDays = learningDayRepository.findFirstDays(userId).stream()
                .collect(toMap(
                        LearningDayRepository.LanguageFirstDay::getLanguage,
                        LearningDayRepository.LanguageFirstDay::getFirstDay));

        List<LanguageRhythm> languages = learnerRepository.findAllByUserIdOrderByLanguage(userId).stream()
                .map(learner -> buildLanguageRhythm(
                        learner,
                        activity.getOrDefault(learner.getLanguage(), Map.of()),
                        firstDays.get(learner.getLanguage()),
                        from))
                .toList();
        return new RhythmResponse(languages);
    }

    @Transactional
    public void updateTarget(UUID userId, Language language, Integer target) {
        Learner learner = requireLearner(userId, language);
        learner.setWeeklyTarget(target);
        learnerRepository.save(learner);
    }

    private LanguageRhythm buildLanguageRhythm(
            Learner learner, Map<LocalDate, LearningDay> activity, LocalDate firstSessionAt, LocalDate bandStart) {
        Integer target = learner.getWeeklyTarget();
        LocalDate setAsideAt =
                learner.getSetAsideAt() == null ? null : LocalDate.ofInstant(learner.getSetAsideAt(), ZoneOffset.UTC);
        LocalDate frozenFrom = setAsideAt == null ? null : setAsideAt.with(DayOfWeek.MONDAY);

        List<RhythmWeek> weeks = new ArrayList<>(BAND_WEEKS);
        for (int week = 0; week < BAND_WEEKS; week++) {
            weeks.add(buildWeek(bandStart.plusWeeks(week), activity, target, frozenFrom));
        }
        LocalDate startedAt = learner.getCreatedAt() == null
                ? bandStart
                : LocalDate.ofInstant(learner.getCreatedAt(), ZoneOffset.UTC);
        PaceSnapshot frozenPace = frozenPace(learner, setAsideAt, firstSessionAt, target);
        return new LanguageRhythm(
                learner.getLanguage(),
                target,
                learner.isActive(),
                startedAt,
                setAsideAt,
                firstSessionAt,
                frozenPace,
                weeks);
    }

    /**
     * The fraction as it stood on the day the language was put down. The rolling window would carry the set-aside
     * date out of range and decay to 0/0, which reads as a fault rather than a record.
     */
    private PaceSnapshot frozenPace(Learner learner, LocalDate setAsideAt, LocalDate firstSessionAt, Integer target) {
        if (setAsideAt == null || firstSessionAt == null) {
            return null;
        }
        LocalDate lastWeek = setAsideAt.with(DayOfWeek.MONDAY);
        LocalDate from = lastWeek.minusWeeks(WEEKS_SHOWN - 1L);
        Map<LocalDate, LearningDay> activity = learningDayRepository
                .findAllByUserIdAndLanguageAndDayBetweenOrderByDayAsc(
                        learner.getUser().getId(), learner.getLanguage(), from, lastWeek.plusDays(6))
                .stream()
                .collect(toMap(LearningDay::getDay, Function.identity()));

        LocalDate countFrom = firstSessionAt.with(DayOfWeek.MONDAY);
        int met = 0;
        int counted = 0;
        for (int week = 0; week < WEEKS_SHOWN; week++) {
            LocalDate weekStart = from.plusWeeks(week);
            if (weekStart.isBefore(countFrom)) {
                continue;
            }
            counted++;
            if (buildWeek(weekStart, activity, target, null).met()) {
                met++;
            }
        }
        return new PaceSnapshot(met, counted);
    }

    private RhythmWeek buildWeek(
            LocalDate weekStart, Map<LocalDate, LearningDay> activity, Integer target, LocalDate frozenFrom) {
        List<RhythmDay> days = new ArrayList<>(7);
        int daysMet = 0;
        for (int offset = 0; offset < 7; offset++) {
            LocalDate date = weekStart.plusDays(offset);
            LearningDay day = activity.get(date);
            boolean met = day != null && day.isMet();
            if (met) {
                daysMet++;
            }
            days.add(new RhythmDay(date, day == null ? 0 : day.getSecondsLearned() / 60, met));
        }
        // A week the language was set aside for is neither met nor missed: nothing was asked of it.
        boolean frozen = frozenFrom != null && !weekStart.isBefore(frozenFrom);
        boolean weekMet = !frozen && target != null && target > 0 && daysMet >= target;
        return new RhythmWeek(weekStart, daysMet, weekMet, frozen, days);
    }

    /**
     * Days belong to the learner's calendar, which only the client knows. Anything further than a time zone away from
     * the server's date is not a time zone, so it falls back to today rather than writing history.
     */
    private LocalDate resolveLocalDate(LocalDate claimed) {
        LocalDate serverToday = LocalDate.now(ZoneOffset.UTC);
        if (claimed == null || claimed.isBefore(serverToday.minusDays(1)) || claimed.isAfter(serverToday.plusDays(1))) {
            return serverToday;
        }
        return claimed;
    }

    /** A learner may only record time against a language they actually study. */
    private Learner requireLearner(UUID userId, Language language) {
        return learnerRepository
                .findByUserIdAndLanguage(userId, language)
                .orElseThrow(() -> new EntityNotFoundException("Learner not found for language: " + language));
    }
}
