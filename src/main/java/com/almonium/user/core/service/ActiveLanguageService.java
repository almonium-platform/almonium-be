package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.learning.rhythm.repository.LearningDayRepository;
import com.almonium.subscription.model.entity.enums.Entitlement;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.dto.response.ActiveLanguagePolicy;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetAsideBy;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.ProfileRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * How many languages may be active at once, and who may change which one it is.
 *
 * <p>A downgrade takes the languages, never the record: the surplus goes read-only and keeps every word. The user
 * picks which one stays; if they never answer, the one they have been reading stays. Afterwards the active language
 * can be changed once a month, and the downgrade pick is not one of those changes.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ActiveLanguageService {
    static final int UNLIMITED = -1;

    PlanValidationService planValidationService;
    PlanService planService;
    LearnerRepository learnerRepository;
    ProfileRepository profileRepository;
    LearningDayRepository learningDayRepository;
    LearningItemRepository learningItemRepository;

    /**
     * Everything the sheet and the switcher need in one read: what is allowed, when it is allowed again, and the
     * languages in the order the choice should be offered.
     */
    @Transactional(readOnly = true)
    public ActiveLanguagePolicy policyFor(User user) {
        List<Learner> learners = learnerRepository.findAllByUserIdOrderByLanguage(user.getId());
        Language pinned = user.getProfile().getDowngradeKeepLanguage();
        List<Learner> ordered = orderByMostRecentlyRead(user.getId(), learners, pinned);
        Map<Language, LocalDate> lastRead = lastReadByLanguage(user.getId());

        // The recommendation is also the silent fallback, so the sheet cannot show one answer and the deadline take
        // another: both read the head of this list.
        Language recommended = ordered.stream()
                .filter(Learner::isActive)
                .map(Learner::getLanguage)
                .findFirst()
                .orElse(null);

        List<ActiveLanguagePolicy.Choice> choices = ordered.stream()
                .map(learner -> new ActiveLanguagePolicy.Choice(
                        learner.getLanguage(),
                        learner.getSelfReportedLevel(),
                        learningItemRepository.countByOwner(learner),
                        lastRead.get(learner.getLanguage()),
                        learner.isActive(),
                        learner.getLanguage() == recommended))
                .toList();

        return new ActiveLanguagePolicy(
                allowance(user), defaultAllowance(), nextSwitchAllowedAt(user).orElse(null), choices);
    }

    /** How many languages this account may keep active, or {@link #UNLIMITED}. Moves when the plan does. */
    public int allowance(User user) {
        return planValidationService.effectiveLimit(user, PlanFeature.MAX_ACTIVE_LANGS);
    }

    /** How many survive once no plan is paying for them: what the downgrade sheet is actually asking about. */
    public int defaultAllowance() {
        return planService.getPlanLimits(Entitlement.FREE).getOrDefault(PlanFeature.MAX_ACTIVE_LANGS, UNLIMITED);
    }

    /**
     * Brings the account inside its allowance, keeping the language the user asked for and otherwise the one they
     * have been reading. Called when entitlement drops — at period end, or when an operator grant is withdrawn.
     */
    public void enforceAllowance(User user) {
        int allowance = allowance(user);
        List<Learner> active = activeLearners(user.getId());
        if (allowance == UNLIMITED || active.size() <= allowance) {
            return;
        }

        Profile profile = user.getProfile();
        List<Learner> ordered = orderByMostRecentlyRead(user.getId(), active, profile.getDowngradeKeepLanguage());
        ordered.stream().skip(allowance).forEach(learner -> {
            learner.setActive(false);
            learner.setSetAsideAt(Instant.now());
            learner.setSetAsideBy(SetAsideBy.SYSTEM);
            learnerRepository.save(learner);
            log.info("Set aside {} for user {}: outside the plan's allowance", learner.getLanguage(), user.getId());
        });

        // The pick is spent whether or not it was made, so a later downgrade asks again rather than reusing an answer.
        profile.setDowngradeKeepLanguage(null);
        profileRepository.save(profile);
    }

    /**
     * Hands back what a downgrade took, most recently set aside first, and never what the user put down themselves.
     */
    public void restoreWhatTheDowngradeTook(User user) {
        int allowance = allowance(user);
        List<Learner> setAsideBySystem = learnerRepository.findAllByUserIdOrderByLanguage(user.getId()).stream()
                .filter(learner -> !learner.isActive() && learner.getSetAsideBy() == SetAsideBy.SYSTEM)
                .sorted(Comparator.comparing(Learner::getSetAsideAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
        if (setAsideBySystem.isEmpty()) {
            return;
        }

        int room = allowance == UNLIMITED
                ? setAsideBySystem.size()
                : Math.max(0, allowance - learnerRepository.countActiveLearnersByUserId(user.getId()));
        setAsideBySystem.stream().limit(room).forEach(learner -> {
            learner.setActive(true);
            learner.setSetAsideAt(null);
            learner.setSetAsideBy(null);
            learnerRepository.save(learner);
            log.info(
                    "Restored {} for user {}: the downgrade took it, not the user",
                    learner.getLanguage(),
                    user.getId());
        });
    }

    /** Remembers which language the user wants kept when the plan ends. Nothing changes until it does. */
    public void chooseWhatToKeep(User user, Language language) {
        learnerRepository
                .findByUserIdAndLanguage(user.getId(), language)
                .filter(Learner::isActive)
                .orElseThrow(() -> new BadUserRequestActionException("That language is not one of your active ones."));

        Profile profile = user.getProfile();
        profile.setDowngradeKeepLanguage(language);
        profileRepository.save(profile);
    }

    /**
     * Makes one language active. At the allowance the switcher is a swap, not an addition, and it may be used once a
     * month — an unlimited switcher would sell serial access to everything the plan withholds.
     */
    public void switchActiveTo(User user, Language language) {
        Learner target = learnerRepository
                .findByUserIdAndLanguage(user.getId(), language)
                .orElseThrow(() -> new BadUserRequestActionException("That language is not one of yours."));
        if (target.isActive()) {
            return;
        }

        int allowance = allowance(user);
        List<Learner> active = activeLearners(user.getId());
        boolean atAllowance = allowance != UNLIMITED && active.size() >= allowance;

        if (atAllowance) {
            nextSwitchAllowedAt(user).ifPresent(allowedAt -> {
                throw new BadUserRequestActionException("You can change your active language again on "
                        + allowedAt.atZone(ZoneOffset.UTC).toLocalDate());
            });
            orderByMostRecentlyRead(user.getId(), active, null).stream()
                    .skip(Math.max(0, allowance - 1))
                    .forEach(learner -> {
                        learner.setActive(false);
                        learner.setSetAsideAt(Instant.now());
                        learner.setSetAsideBy(SetAsideBy.USER);
                        learnerRepository.save(learner);
                    });
        }

        target.setActive(true);
        target.setSetAsideAt(null);
        target.setSetAsideBy(null);
        learnerRepository.save(target);

        if (atAllowance) {
            Profile profile = user.getProfile();
            profile.setLastActiveSwitchAt(Instant.now());
            profileRepository.save(profile);
        }
    }

    /**
     * Gives the monthly switch back, whatever the calendar says: an operator action for support and for testing a
     * flow whose cooldown otherwise outlasts the session.
     */
    public void resetSwitchCooldown(User user, User operator) {
        Profile profile = user.getProfile();
        if (profile.getLastActiveSwitchAt() == null) {
            return;
        }
        profile.setLastActiveSwitchAt(null);
        profileRepository.save(profile);
        log.info("Operator {} cleared the active-language switch cooldown for user {}", operator.getId(), user.getId());
    }

    /** When the next switch becomes available, or empty when one is available now. */
    public Optional<Instant> nextSwitchAllowedAt(User user) {
        Instant last = user.getProfile().getLastActiveSwitchAt();
        if (last == null) {
            return Optional.empty();
        }
        // A month from the switch itself, not from the month it fell in: the boundary version let a switch on the
        // 31st come back on the 1st, which is one Reddit comment away from being a known trick.
        Instant nextAllowed = last.atZone(ZoneOffset.UTC).plusMonths(1).toInstant();
        return nextAllowed.isAfter(Instant.now()) ? Optional.of(nextAllowed) : Optional.empty();
    }

    private List<Learner> activeLearners(java.util.UUID userId) {
        return learnerRepository.findAllByUserIdOrderByLanguage(userId).stream()
                .filter(Learner::isActive)
                .toList();
    }

    /**
     * Most recently read first, with the user's pick ahead of everything. Word counts and last-read dates are what
     * make this choice answerable, so the same ordering drives both the sheet and the silent fallback.
     */
    private List<Learner> orderByMostRecentlyRead(java.util.UUID userId, List<Learner> learners, Language pinned) {
        Map<Language, LocalDate> lastRead = lastReadByLanguage(userId);
        Comparator<Learner> byPin = Comparator.comparing(learner -> learner.getLanguage() != pinned);
        Comparator<Learner> byLastRead = Comparator.comparing(
                (Learner learner) -> lastRead.get(learner.getLanguage()),
                Comparator.nullsLast(Comparator.reverseOrder()));
        return learners.stream()
                .sorted(byPin.thenComparing(byLastRead).thenComparing(Learner::getLanguage))
                .toList();
    }

    public Map<Language, LocalDate> lastReadByLanguage(java.util.UUID userId) {
        Map<Language, LocalDate> lastRead = new HashMap<>();
        learningDayRepository.findLastDays(userId).forEach(row -> lastRead.put(row.getLanguage(), row.getLastDay()));
        return lastRead;
    }
}
