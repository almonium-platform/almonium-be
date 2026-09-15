package com.almonium.learning.rhythm.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.rhythm.dto.request.LearningActivityRequest;
import com.almonium.learning.rhythm.dto.response.LanguageRhythm;
import com.almonium.learning.rhythm.dto.response.PaceSnapshot;
import com.almonium.learning.rhythm.dto.response.RhythmResponse;
import com.almonium.learning.rhythm.dto.response.RhythmWeek;
import com.almonium.learning.rhythm.model.ActivitySource;
import com.almonium.learning.rhythm.model.LearningDay;
import com.almonium.learning.rhythm.repository.LearningDayRepository;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.repository.LearnerRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class LearningRhythmServiceTest {
    static final UUID USER_ID = UUID.randomUUID();
    static final LocalDate TODAY = LocalDate.now(ZoneOffset.UTC);
    static final LocalDate MONDAY = TODAY.with(DayOfWeek.MONDAY);

    @Mock
    LearningDayRepository learningDayRepository;

    @Mock
    LearnerRepository learnerRepository;

    @InjectMocks
    LearningRhythmService learningRhythmService;

    @DisplayName("Should return fourteen Monday-aligned weeks ending with the week in progress")
    @Test
    void givenNoActivity_whenGetRhythm_thenBandCoversFourteenWeeksEndingToday() {
        givenLearners(learner(Language.DE, 2, true));
        givenActivity();

        List<RhythmWeek> weeks =
                onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY)).weeks();

        assertThat(weeks).hasSize(14);
        assertThat(weeks).allSatisfy(week -> {
            assertThat(week.weekStart().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
            assertThat(week.days()).hasSize(7);
        });
        assertThat(weeks.getLast().weekStart()).isEqualTo(MONDAY);
        assertThat(weeks.getFirst().weekStart()).isEqualTo(MONDAY.minusWeeks(13));
    }

    @DisplayName("Should keep each language's days to its own band")
    @Test
    void givenTwoLanguages_whenGetRhythm_thenDaysAreNotSharedBetweenThem() {
        givenLearners(learner(Language.DE, 2, true), learner(Language.ES, 2, true));
        givenActivity(
                day(Language.DE, MONDAY, 1500),
                day(Language.DE, MONDAY.plusDays(1), 900),
                day(Language.ES, MONDAY, 600));

        RhythmResponse rhythm = learningRhythmService.getRhythm(USER_ID, TODAY);

        assertThat(currentWeek(rhythm, Language.DE).daysMet()).isEqualTo(2);
        assertThat(currentWeek(rhythm, Language.DE).met()).isTrue();
        assertThat(currentWeek(rhythm, Language.ES).daysMet()).isEqualTo(1);
        assertThat(currentWeek(rhythm, Language.ES).met()).isFalse();
    }

    @DisplayName("Should judge each language against its own bar")
    @Test
    void givenDifferentTargetsPerLanguage_whenGetRhythm_thenEachIsJudgedSeparately() {
        givenLearners(learner(Language.DE, 2, true), learner(Language.ES, 4, true));
        givenActivity(
                day(Language.DE, MONDAY, 900),
                day(Language.DE, MONDAY.plusDays(1), 900),
                day(Language.ES, MONDAY, 900),
                day(Language.ES, MONDAY.plusDays(1), 900));

        assertThat(currentWeek(learningRhythmService.getRhythm(USER_ID, TODAY), Language.DE)
                        .met())
                .isTrue();
        assertThat(currentWeek(learningRhythmService.getRhythm(USER_ID, TODAY), Language.ES)
                        .met())
                .isFalse();
    }

    @DisplayName("Should report when a language was taken up, so its weeks are counted from there")
    @Test
    void givenALanguageTakenUpRecently_whenGetRhythm_thenItsStartDateIsReported() {
        Learner recent = learner(Language.DE, 2, true);
        recent.setCreatedAt(TODAY.minusWeeks(4).atStartOfDay(ZoneOffset.UTC).toInstant());
        givenLearners(recent);
        givenActivity();

        assertThat(onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY)).startedAt())
                .isEqualTo(TODAY.minusWeeks(4));
    }

    @DisplayName("Should keep a set-aside language's record, read-only")
    @Test
    void givenInactiveLearner_whenGetRhythm_thenTargetSurvivesButIsNotEditable() {
        givenLearners(learner(Language.DE, 2, false));
        givenActivity(day(Language.DE, MONDAY, 900));

        LanguageRhythm rhythm = onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY));

        assertThat(rhythm.target()).isEqualTo(2);
        assertThat(rhythm.editable()).isFalse();
    }

    @DisplayName("Should freeze the weeks since a language was set aside rather than count them as missed")
    @Test
    void givenALanguageSetAsideTwoWeeksAgo_whenGetRhythm_thenThoseWeeksAreFrozenNotMissed() {
        Learner setAside = learner(Language.DE, 2, false);
        setAside.setSetAsideAt(MONDAY.minusWeeks(2).atStartOfDay(ZoneOffset.UTC).toInstant());
        givenLearners(setAside);
        givenActivity(
                day(Language.DE, MONDAY.minusWeeks(3), 900),
                day(Language.DE, MONDAY.minusWeeks(3).plusDays(1), 900));

        List<RhythmWeek> weeks =
                onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY)).weeks();
        RhythmWeek beforeSetAside = weeks.get(weeks.size() - 4);
        RhythmWeek afterSetAside = weeks.getLast();

        assertThat(beforeSetAside.frozen()).isFalse();
        assertThat(beforeSetAside.met()).isTrue();
        assertThat(afterSetAside.frozen()).isTrue();
        assertThat(afterSetAside.met()).isFalse();
    }

    @DisplayName("Should report the first day a language was ever learned, where its weeks start counting")
    @Test
    void givenActivity_whenGetRhythm_thenTheFirstSessionIsReported() {
        givenLearners(learner(Language.DE, 2, true));
        givenActivity(day(Language.DE, MONDAY.minusWeeks(2), 900), day(Language.DE, MONDAY, 900));

        assertThat(onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY)).firstSessionAt())
                .isEqualTo(MONDAY.minusWeeks(2));
    }

    @DisplayName("Should report no first session for a language never learned in")
    @Test
    void givenNoActivityEver_whenGetRhythm_thenNoFirstSessionIsReported() {
        givenLearners(learner(Language.DE, 2, true));
        givenActivity();

        assertThat(onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY)).firstSessionAt())
                .isNull();
    }

    @DisplayName("Should freeze the fraction at the set-aside date rather than let it decay to nothing")
    @Test
    void givenALanguageLongSetAside_whenGetRhythm_thenTheFractionIsTheOneItHadWhenPutDown() {
        Learner setAside = learner(Language.DE, 2, false);
        setAside.setSetAsideAt(MONDAY.minusWeeks(2).atStartOfDay(ZoneOffset.UTC).toInstant());
        givenLearners(setAside);
        givenActivity(
                day(Language.DE, MONDAY.minusWeeks(3), 900),
                day(Language.DE, MONDAY.minusWeeks(3).plusDays(1), 900));
        when(learningDayRepository.findAllByUserIdAndLanguageAndDayBetweenOrderByDayAsc(
                        any(), eq(Language.DE), any(), any()))
                .thenReturn(List.of(
                        day(Language.DE, MONDAY.minusWeeks(3), 900),
                        day(Language.DE, MONDAY.minusWeeks(3).plusDays(1), 900)));

        PaceSnapshot frozen =
                onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY)).frozenPace();

        assertThat(frozen).isNotNull();
        // one week kept, out of the two the language was alive for before it was put down
        assertThat(frozen.met()).isEqualTo(1);
        assertThat(frozen.counted()).isEqualTo(2);
    }

    @DisplayName("Should leave an active language without a frozen fraction")
    @Test
    void givenAnActiveLanguage_whenGetRhythm_thenThereIsNoFrozenFraction() {
        givenLearners(learner(Language.DE, 2, true));
        givenActivity(day(Language.DE, MONDAY, 900));

        assertThat(onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY)).frozenPace())
                .isNull();
    }

    @DisplayName("Should report when a language was set aside, so the copy can name the date")
    @Test
    void givenASetAsideLanguage_whenGetRhythm_thenTheDateIsReported() {
        Learner setAside = learner(Language.DE, 2, false);
        setAside.setSetAsideAt(MONDAY.minusWeeks(2).atStartOfDay(ZoneOffset.UTC).toInstant());
        givenLearners(setAside);
        givenActivity();

        assertThat(onlyLanguage(learningRhythmService.getRhythm(USER_ID, TODAY)).setAsideAt())
                .isEqualTo(MONDAY.minusWeeks(2));
    }

    @DisplayName("Should stop accruing learning days once a language is set aside")
    @Test
    void givenASetAsideLanguage_whenRecordActivity_thenNothingIsWritten() {
        Learner setAside = learner(Language.DE, 2, false);
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.DE)).thenReturn(Optional.of(setAside));

        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.READ, Language.DE, 600, TODAY, false));

        verify(learningDayRepository, never())
                .accumulate(any(), any(), any(), anyString(), anyInt(), any(Boolean.class), anyInt());
    }

    @DisplayName("Should never judge a week when no target is set")
    @Test
    void givenNoTarget_whenGetRhythm_thenWeekIsNeverMet() {
        givenLearners(learner(Language.DE, null, true));
        givenActivity(day(Language.DE, MONDAY, 1500), day(Language.DE, MONDAY.plusDays(1), 600));

        RhythmWeek week = currentWeek(learningRhythmService.getRhythm(USER_ID, TODAY), Language.DE);

        assertThat(week.daysMet()).isEqualTo(2);
        assertThat(week.met()).isFalse();
    }

    @DisplayName("Should treat an explicit no-target as a legitimate choice rather than an unreachable bar")
    @Test
    void givenNoTargetChosen_whenGetRhythm_thenWeekIsNotMet() {
        givenLearners(learner(Language.DE, 0, true));
        givenActivity(day(Language.DE, MONDAY, 1500));

        assertThat(currentWeek(learningRhythmService.getRhythm(USER_ID, TODAY), Language.DE)
                        .met())
                .isFalse();
    }

    @DisplayName("Should report minutes as texture, leaving a sub-minute day met all the same")
    @Test
    void givenShortAndLongDays_whenGetRhythm_thenMinutesAreReportedWithoutAffectingMet() {
        givenLearners(learner(Language.DE, 2, true));
        givenActivity(day(Language.DE, MONDAY, 30), day(Language.DE, MONDAY.plusDays(1), 1500));

        RhythmWeek week = currentWeek(learningRhythmService.getRhythm(USER_ID, TODAY), Language.DE);

        assertThat(week.days().getFirst().minutes()).isZero();
        assertThat(week.days().getFirst().met()).isTrue();
        assertThat(week.days().get(1).minutes()).isEqualTo(25);
        assertThat(week.met()).isTrue();
    }

    @DisplayName("Should cap a single report so a forgotten tab cannot claim a deep day")
    @Test
    void givenAnImplausiblyLongReport_whenRecordActivity_thenSecondsAreCapped() {
        givenLearner(Language.DE);

        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.READ, Language.DE, 3600, TODAY, false));

        ArgumentCaptor<Integer> seconds = ArgumentCaptor.forClass(Integer.class);
        verify(learningDayRepository)
                .accumulate(any(), eq(USER_ID), eq(TODAY), eq("DE"), seconds.capture(), eq(true), anyInt());
        assertThat(seconds.getValue()).isEqualTo(LearningRhythmService.MAX_SECONDS_PER_REPORT);
    }

    @DisplayName("Should spend the daily cap across the account rather than per language")
    @Test
    void whenRecordActivity_thenTheDailyCapIsTheAccountWideOne() {
        givenLearner(Language.DE);

        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.READ, Language.DE, 60, TODAY, false));

        verify(learningDayRepository)
                .accumulate(
                        any(),
                        eq(USER_ID),
                        eq(TODAY),
                        eq("DE"),
                        eq(60),
                        eq(true),
                        eq(LearningRhythmService.MAX_SECONDS_PER_DAY));
    }

    @DisplayName("Should mark the day met on a completed event that carried no measured time")
    @Test
    void givenCompletedEventWithoutSeconds_whenRecordActivity_thenDayIsMarkedMet() {
        givenLearner(Language.DE);

        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.REVIEW, Language.DE, 0, TODAY, true));

        verify(learningDayRepository).accumulate(any(), eq(USER_ID), eq(TODAY), eq("DE"), eq(0), eq(true), anyInt());
    }

    @DisplayName("Should ignore a report that carries neither time nor a completed event")
    @Test
    void givenEmptyReport_whenRecordActivity_thenNothingIsWritten() {
        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.PLAY, Language.DE, 0, TODAY, false));

        verify(learningDayRepository, never())
                .accumulate(any(), any(), any(), anyString(), anyInt(), any(Boolean.class), anyInt());
    }

    @DisplayName("Should fall back to the server's date when the client claims one no time zone could produce")
    @Test
    void givenImplausibleClientDate_whenRecordActivity_thenServerDateIsUsed() {
        givenLearner(Language.DE);

        learningRhythmService.recordActivity(
                USER_ID,
                new LearningActivityRequest(ActivitySource.READ, Language.DE, 60, LocalDate.of(2020, 1, 1), false));

        verify(learningDayRepository).accumulate(any(), eq(USER_ID), eq(TODAY), eq("DE"), eq(60), eq(true), anyInt());
    }

    private void givenLearners(Learner... learners) {
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID)).thenReturn(List.of(learners));
    }

    private void givenLearner(Language language) {
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, language))
                .thenReturn(Optional.of(learner(language, 2, true)));
    }

    private void givenActivity(LearningDay... days) {
        when(learningDayRepository.findAllByUserIdAndDayBetweenOrderByDayAsc(eq(USER_ID), any(), any()))
                .thenReturn(List.of(days));
        when(learningDayRepository.findFirstDays(USER_ID)).thenReturn(firstDays(days));
    }

    private List<LearningDayRepository.LanguageFirstDay> firstDays(LearningDay... days) {
        Map<Language, LocalDate> earliest = new java.util.EnumMap<>(Language.class);
        for (LearningDay day : days) {
            earliest.merge(day.getLanguage(), day.getDay(), (a, b) -> a.isBefore(b) ? a : b);
        }
        return earliest.entrySet().stream()
                .map(entry -> (LearningDayRepository.LanguageFirstDay) new LearningDayRepository.LanguageFirstDay() {
                    @Override
                    public Language getLanguage() {
                        return entry.getKey();
                    }

                    @Override
                    public LocalDate getFirstDay() {
                        return entry.getValue();
                    }
                })
                .toList();
    }

    private Learner learner(Language language, Integer target, boolean active) {
        Learner learner = new Learner();
        learner.setUser(
                com.almonium.user.core.model.entity.User.builder().id(USER_ID).build());
        learner.setLanguage(language);
        learner.setWeeklyTarget(target);
        learner.setActive(active);
        learner.setCreatedAt(TODAY.minusWeeks(30).atStartOfDay(ZoneOffset.UTC).toInstant());
        return learner;
    }

    private LearningDay day(Language language, LocalDate date, int seconds) {
        return LearningDay.builder()
                .id(UUID.randomUUID())
                .language(language)
                .day(date)
                .secondsLearned(seconds)
                .met(true)
                .build();
    }

    private LanguageRhythm onlyLanguage(RhythmResponse rhythm) {
        assertThat(rhythm.languages()).hasSize(1);
        return rhythm.languages().getFirst();
    }

    private RhythmWeek currentWeek(RhythmResponse rhythm, Language language) {
        return rhythm.languages().stream()
                .filter(entry -> entry.language() == language)
                .findFirst()
                .orElseThrow()
                .weeks()
                .getLast();
    }
}
