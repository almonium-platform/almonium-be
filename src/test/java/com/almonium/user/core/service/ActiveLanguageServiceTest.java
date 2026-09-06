package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.rhythm.repository.LearningDayRepository;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetAsideBy;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.util.TestDataGenerator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class ActiveLanguageServiceTest {
    private static final UUID USER_ID = UUID.randomUUID();

    @Mock
    PlanValidationService planValidationService;

    @Mock
    LearnerRepository learnerRepository;

    @Mock
    ProfileRepository profileRepository;

    @Mock
    LearningDayRepository learningDayRepository;

    @Mock
    com.almonium.card.core.repository.LearningItemRepository learningItemRepository;

    @InjectMocks
    ActiveLanguageService activeLanguageService;

    User user;
    Profile profile;

    @BeforeEach
    void setUp() {
        user = TestDataGenerator.buildTestUserWithId(USER_ID);
        profile = Profile.builder().user(user).build();
        user.setProfile(profile);
        lenient().when(learningDayRepository.findLastDays(USER_ID)).thenReturn(List.of());
    }

    @DisplayName("Should keep the language the user picked and set the rest aside as the system")
    @Test
    void givenDowngradePick_whenEnforceAllowance_thenPickSurvivesAndRestAreSystemSetAside() {
        profile.setDowngradeKeepLanguage(Language.IT);
        List<Learner> learners = List.of(active(Language.DE), active(Language.EN), active(Language.IT));
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(1);
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID)).thenReturn(learners);

        activeLanguageService.enforceAllowance(user);

        assertThat(learners)
                .filteredOn(Learner::isActive)
                .extracting(Learner::getLanguage)
                .containsExactly(Language.IT);
        assertThat(learners)
                .filteredOn(learner -> !learner.isActive())
                .allMatch(learner -> learner.getSetAsideBy() == SetAsideBy.SYSTEM);
        // Choosing what to keep is not spending a switch.
        assertThat(profile.getLastActiveSwitchAt()).isNull();
        assertThat(profile.getDowngradeKeepLanguage()).isNull();
    }

    @DisplayName("Should fall back to the most recently read language when the sheet is never answered")
    @Test
    void givenNoPick_whenEnforceAllowance_thenMostRecentlyReadSurvives() {
        List<Learner> learners = List.of(active(Language.DE), active(Language.EN));
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(1);
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID)).thenReturn(learners);
        when(learningDayRepository.findLastDays(USER_ID))
                .thenReturn(List.of(
                        lastDay(Language.DE, LocalDate.now().minusMonths(2)), lastDay(Language.EN, LocalDate.now())));

        activeLanguageService.enforceAllowance(user);

        assertThat(learners)
                .filteredOn(Learner::isActive)
                .extracting(Learner::getLanguage)
                .containsExactly(Language.EN);
    }

    @DisplayName("Should leave an account inside its allowance alone")
    @Test
    void givenUnlimitedAllowance_whenEnforceAllowance_thenNothingIsSetAside() {
        List<Learner> learners = List.of(active(Language.DE), active(Language.EN));
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(-1);
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID)).thenReturn(learners);

        activeLanguageService.enforceAllowance(user);

        assertThat(learners).allMatch(Learner::isActive);
    }

    @DisplayName("Should return only what the downgrade took, never what the user put down")
    @Test
    void givenBothKindsOfSetAside_whenRestoring_thenOnlySystemOnesComeBack() {
        Learner takenByDowngrade = setAside(Language.EN, SetAsideBy.SYSTEM, Instant.now());
        Learner putDownByUser = setAside(Language.IT, SetAsideBy.USER, Instant.now());
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(-1);
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID))
                .thenReturn(List.of(active(Language.DE), takenByDowngrade, putDownByUser));

        activeLanguageService.restoreWhatTheDowngradeTook(user);

        assertThat(takenByDowngrade.isActive()).isTrue();
        assertThat(takenByDowngrade.getSetAsideBy()).isNull();
        assertThat(putDownByUser.isActive()).isFalse();
    }

    @DisplayName("Should swap rather than add when the account is at its allowance, and spend the monthly switch")
    @Test
    void givenOneAllowed_whenSwitching_thenTheOtherIsSetAsideAndTheSwitchIsSpent() {
        Learner current = active(Language.DE);
        Learner wanted = setAside(Language.EN, SetAsideBy.SYSTEM, Instant.now());
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(1);
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.EN)).thenReturn(java.util.Optional.of(wanted));
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID)).thenReturn(List.of(current, wanted));

        activeLanguageService.switchActiveTo(user, Language.EN);

        assertThat(wanted.isActive()).isTrue();
        assertThat(current.isActive()).isFalse();
        assertThat(current.getSetAsideBy()).isEqualTo(SetAsideBy.USER);
        assertThat(profile.getLastActiveSwitchAt()).isNotNull();
    }

    @DisplayName("Should refuse a second switch inside a month of the last one")
    @Test
    void givenSwitchAlreadySpentThisMonth_whenSwitchingAgain_thenItIsRefused() {
        profile.setLastActiveSwitchAt(Instant.now());
        Learner wanted = setAside(Language.EN, SetAsideBy.USER, Instant.now());
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(1);
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.EN)).thenReturn(java.util.Optional.of(wanted));
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID))
                .thenReturn(List.of(active(Language.DE), wanted));

        assertThatThrownBy(() -> activeLanguageService.switchActiveTo(user, Language.EN))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("again on");
        assertThat(wanted.isActive()).isFalse();
    }

    @DisplayName("Should still refuse a switch 25 days on, whatever the calendar did in between")
    @Test
    void givenSwitchSpentTwentyFiveDaysAgo_whenSwitchingAgain_thenItIsRefused() {
        // The month-boundary version handed back a switch on the 1st to anyone who spent one on the 31st. A month
        // is counted from the switch itself, so this holds on every date rather than most of them.
        profile.setLastActiveSwitchAt(Instant.now().minus(25, ChronoUnit.DAYS));
        Learner wanted = setAside(Language.EN, SetAsideBy.USER, Instant.now());
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(1);
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.EN)).thenReturn(java.util.Optional.of(wanted));
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID))
                .thenReturn(List.of(active(Language.DE), wanted));

        assertThatThrownBy(() -> activeLanguageService.switchActiveTo(user, Language.EN))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("again on");
        assertThat(wanted.isActive()).isFalse();
    }

    @DisplayName("Should allow the switch again once the month has turned")
    @Test
    void givenLastSwitchLastMonth_whenSwitching_thenItIsAllowed() {
        profile.setLastActiveSwitchAt(Instant.now().minus(70, ChronoUnit.DAYS));
        Learner wanted = setAside(Language.EN, SetAsideBy.USER, Instant.now());
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(1);
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.EN)).thenReturn(java.util.Optional.of(wanted));
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID))
                .thenReturn(List.of(active(Language.DE), wanted));

        activeLanguageService.switchActiveTo(user, Language.EN);

        assertThat(wanted.isActive()).isTrue();
    }

    @DisplayName("Should hand the switch back when an operator clears the cooldown")
    @Test
    void givenSwitchSpentThisMonth_whenOperatorResetsCooldown_thenSwitchingIsAllowedAgain() {
        profile.setLastActiveSwitchAt(Instant.now());
        Learner wanted = setAside(Language.EN, SetAsideBy.USER, Instant.now());
        when(planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS))
                .thenReturn(1);
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.EN)).thenReturn(java.util.Optional.of(wanted));
        when(learnerRepository.findAllByUserIdOrderByLanguage(USER_ID))
                .thenReturn(List.of(active(Language.DE), wanted));

        activeLanguageService.resetSwitchCooldown(user, TestDataGenerator.buildTestUserWithId(UUID.randomUUID()));
        activeLanguageService.switchActiveTo(user, Language.EN);

        assertThat(profile.getLastActiveSwitchAt()).isNotNull();
        assertThat(wanted.isActive()).isTrue();
    }

    @DisplayName("Should not let a language the user does not have be kept")
    @Test
    void givenForeignLanguage_whenChoosingWhatToKeep_thenItIsRejected() {
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.IT)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> activeLanguageService.chooseWhatToKeep(user, Language.IT))
                .isInstanceOf(BadUserRequestActionException.class);
    }

    private Learner active(Language language) {
        Learner learner = new Learner(user, language, null);
        learner.setId(UUID.randomUUID());
        return learner;
    }

    private Learner setAside(Language language, SetAsideBy by, Instant at) {
        Learner learner = active(language);
        learner.setActive(false);
        learner.setSetAsideBy(by);
        learner.setSetAsideAt(at);
        return learner;
    }

    private LearningDayRepository.LanguageLastDay lastDay(Language language, LocalDate day) {
        return new LearningDayRepository.LanguageLastDay() {
            @Override
            public Language getLanguage() {
                return language;
            }

            @Override
            public LocalDate getLastDay() {
                return day;
            }
        };
    }
}
