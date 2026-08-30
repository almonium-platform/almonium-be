package com.almonium.learning.rhythm.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.learning.rhythm.dto.request.LearningActivityRequest;
import com.almonium.learning.rhythm.dto.response.RhythmResponse;
import com.almonium.learning.rhythm.dto.response.RhythmWeek;
import com.almonium.learning.rhythm.model.ActivitySource;
import com.almonium.learning.rhythm.model.LearningDay;
import com.almonium.learning.rhythm.repository.LearningDayRepository;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.ProfileRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
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
    ProfileRepository profileRepository;

    @InjectMocks
    LearningRhythmService learningRhythmService;

    @DisplayName("Should return fourteen Monday-aligned weeks ending with the week in progress")
    @Test
    void givenNoActivity_whenGetRhythm_thenBandCoversFourteenWeeksEndingToday() {
        givenTarget(3);
        givenActivity();

        RhythmResponse rhythm = learningRhythmService.getRhythm(USER_ID, TODAY);

        assertThat(rhythm.weeks()).hasSize(14);
        assertThat(rhythm.weeks()).allSatisfy(week -> {
            assertThat(week.weekStart().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
            assertThat(week.days()).hasSize(7);
        });
        assertThat(rhythm.weeks().getLast().weekStart()).isEqualTo(MONDAY);
        assertThat(rhythm.weeks().getFirst().weekStart()).isEqualTo(MONDAY.minusWeeks(13));
    }

    @DisplayName("Should clear the week once the learner's own bar is reached")
    @Test
    void givenThreeDaysMetAgainstTargetOfThree_whenGetRhythm_thenWeekIsMet() {
        givenTarget(3);
        givenActivity(day(MONDAY, 1500), day(MONDAY.plusDays(1), 600), day(MONDAY.plusDays(2), 30));

        RhythmWeek week = currentWeek(learningRhythmService.getRhythm(USER_ID, TODAY));

        assertThat(week.daysMet()).isEqualTo(3);
        assertThat(week.met()).isTrue();
    }

    @DisplayName("Should never judge a week when no target is set")
    @Test
    void givenNoTarget_whenGetRhythm_thenWeekIsNeverMet() {
        givenTarget(null);
        givenActivity(day(MONDAY, 1500), day(MONDAY.plusDays(1), 600), day(MONDAY.plusDays(2), 900));

        RhythmWeek week = currentWeek(learningRhythmService.getRhythm(USER_ID, TODAY));

        assertThat(week.daysMet()).isEqualTo(3);
        assertThat(week.met()).isFalse();
    }

    @DisplayName("Should treat an explicit no-target as a legitimate choice rather than an unreachable bar")
    @Test
    void givenNoTargetChosen_whenGetRhythm_thenWeekIsNotMet() {
        givenTarget(0);
        givenActivity(day(MONDAY, 1500));

        assertThat(currentWeek(learningRhythmService.getRhythm(USER_ID, TODAY)).met())
                .isFalse();
    }

    @DisplayName("Should report minutes as texture, leaving a sub-minute day met all the same")
    @Test
    void givenShortAndLongDays_whenGetRhythm_thenMinutesAreReportedWithoutAffectingMet() {
        givenTarget(2);
        givenActivity(day(MONDAY, 30), day(MONDAY.plusDays(1), 1500));

        List<RhythmWeek> weeks = learningRhythmService.getRhythm(USER_ID, TODAY).weeks();
        RhythmWeek week = weeks.getLast();

        assertThat(week.days().getFirst().minutes()).isZero();
        assertThat(week.days().getFirst().met()).isTrue();
        assertThat(week.days().get(1).minutes()).isEqualTo(25);
        assertThat(week.met()).isTrue();
    }

    @DisplayName("Should cap a single report so a forgotten tab cannot claim a deep day")
    @Test
    void givenAnImplausiblyLongReport_whenRecordActivity_thenSecondsAreCapped() {
        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.READ, 3600, TODAY, false));

        ArgumentCaptor<Integer> seconds = ArgumentCaptor.forClass(Integer.class);
        verify(learningDayRepository).accumulate(any(), eq(USER_ID), eq(TODAY), seconds.capture(), eq(true), anyInt());
        assertThat(seconds.getValue()).isEqualTo(LearningRhythmService.MAX_SECONDS_PER_REPORT);
    }

    @DisplayName("Should mark the day met on a completed event that carried no measured time")
    @Test
    void givenCompletedEventWithoutSeconds_whenRecordActivity_thenDayIsMarkedMet() {
        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.REVIEW, 0, TODAY, true));

        verify(learningDayRepository).accumulate(any(), eq(USER_ID), eq(TODAY), eq(0), eq(true), anyInt());
    }

    @DisplayName("Should ignore a report that carries neither time nor a completed event")
    @Test
    void givenEmptyReport_whenRecordActivity_thenNothingIsWritten() {
        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.PLAY, 0, TODAY, false));

        verify(learningDayRepository, never()).accumulate(any(), any(), any(), anyInt(), any(Boolean.class), anyInt());
    }

    @DisplayName("Should fall back to the server's date when the client claims one no time zone could produce")
    @Test
    void givenImplausibleClientDate_whenRecordActivity_thenServerDateIsUsed() {
        learningRhythmService.recordActivity(
                USER_ID, new LearningActivityRequest(ActivitySource.READ, 60, LocalDate.of(2020, 1, 1), false));

        verify(learningDayRepository).accumulate(any(), eq(USER_ID), eq(TODAY), eq(60), eq(true), anyInt());
    }

    private void givenTarget(Integer target) {
        Profile profile = new Profile();
        profile.setWeeklyTarget(target);
        when(profileRepository.findById(USER_ID)).thenReturn(Optional.of(profile));
    }

    private void givenActivity(LearningDay... days) {
        when(learningDayRepository.findAllByUserIdAndDayBetweenOrderByDayAsc(eq(USER_ID), any(), any()))
                .thenReturn(List.of(days));
    }

    private LearningDay day(LocalDate date, int seconds) {
        return LearningDay.builder()
                .id(UUID.randomUUID())
                .day(date)
                .secondsLearned(seconds)
                .met(true)
                .build();
    }

    private RhythmWeek currentWeek(RhythmResponse rhythm) {
        return rhythm.weeks().getLast();
    }
}
