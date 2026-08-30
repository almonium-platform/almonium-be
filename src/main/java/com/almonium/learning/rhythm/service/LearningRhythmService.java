package com.almonium.learning.rhythm.service;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.rhythm.dto.request.LearningActivityRequest;
import com.almonium.learning.rhythm.dto.response.RhythmDay;
import com.almonium.learning.rhythm.dto.response.RhythmResponse;
import com.almonium.learning.rhythm.dto.response.RhythmWeek;
import com.almonium.learning.rhythm.model.LearningDay;
import com.almonium.learning.rhythm.repository.LearningDayRepository;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.ProfileRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The harness: whether the learner cleared the bar they set for themselves, week by week.
 *
 * <p>Weeks are the unit, so a missed Tuesday is not a failure and nothing can be lost. A day counts as met on any
 * completed learning event, whatever it was; the minutes accumulated alongside are texture for the band's tint only.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class LearningRhythmService {
    /** Weeks in the band: thirteen finished weeks plus the one in progress. */
    static final int BAND_WEEKS = 14;

    /** A single report cannot claim more than this, however long a tab was left open. */
    static final int MAX_SECONDS_PER_REPORT = 600;

    static final int MAX_SECONDS_PER_DAY = 12 * 60 * 60;

    LearningDayRepository learningDayRepository;
    ProfileRepository profileRepository;

    @Transactional
    public void recordActivity(UUID userId, LearningActivityRequest request) {
        int seconds = Math.min(Math.max(request.seconds(), 0), MAX_SECONDS_PER_REPORT);
        boolean met = request.completed() || seconds > 0;
        if (!met) {
            return;
        }
        learningDayRepository.accumulate(
                UuidCreator.getTimeOrderedEpoch(),
                userId,
                resolveLocalDate(request.localDate()),
                seconds,
                true,
                MAX_SECONDS_PER_DAY);
    }

    public RhythmResponse getRhythm(UUID userId, LocalDate today) {
        LocalDate currentWeekStart = resolveLocalDate(today).with(DayOfWeek.MONDAY);
        LocalDate from = currentWeekStart.minusWeeks(BAND_WEEKS - 1L);
        LocalDate to = currentWeekStart.plusDays(6);

        Map<LocalDate, LearningDay> activity =
                learningDayRepository.findAllByUserIdAndDayBetweenOrderByDayAsc(userId, from, to).stream()
                        .collect(toMap(LearningDay::getDay, identity()));

        Integer target = findProfile(userId).getWeeklyTarget();
        List<RhythmWeek> weeks = new ArrayList<>(BAND_WEEKS);
        for (int week = 0; week < BAND_WEEKS; week++) {
            weeks.add(buildWeek(from.plusWeeks(week), activity, target));
        }
        return new RhythmResponse(target, weeks);
    }

    @Transactional
    public void updateTarget(UUID userId, Integer target) {
        Profile profile = findProfile(userId);
        profile.setWeeklyTarget(target);
        profileRepository.save(profile);
    }

    private RhythmWeek buildWeek(LocalDate weekStart, Map<LocalDate, LearningDay> activity, Integer target) {
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
        boolean weekMet = target != null && target > 0 && daysMet >= target;
        return new RhythmWeek(weekStart, daysMet, weekMet, days);
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

    private Profile findProfile(UUID userId) {
        return profileRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found: " + userId));
    }
}
