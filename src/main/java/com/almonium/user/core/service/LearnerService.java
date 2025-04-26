package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.CardService;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import com.almonium.user.core.dto.request.UpdateLearnerRequest;
import com.almonium.user.core.dto.response.LearnerDto;
import com.almonium.user.core.events.UserAddedTargetLanguageEvent;
import com.almonium.user.core.events.UserRemovedTargetLanguageEvent;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.mapper.LearnerMapper;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerService {
    PlanValidationService planValidationService;
    CardService cardService;

    LearnerRepository learnerRepository;
    UserRepository userRepository;

    LearnerMapper learnerMapper;
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public void updateLearner(UUID userId, Language code, UpdateLearnerRequest request) {
        var learner = learnerRepository
                .findByUserIdAndLanguage(userId, code)
                .orElseThrow(() -> new EntityNotFoundException("Learner not found."));

        if (request.active() != null) {
            long activeLearners = learnerRepository.countActiveLearnersByUserId(userId);
            if (!request.active() && activeLearners == 1) {
                throw new BadUserRequestActionException("At least one target language must be active.");
            }

            learner.setActive(request.active());
            log.info("Learner {} is now {}.", learner.getId(), request.active() ? "active" : "inactive");
        }

        if (request.level() != null) {
            learner.setSelfReportedLevel(request.level());
            log.info("Learner {} CEFR level updated to {}.", learner.getId(), request.level());
        }

        learnerRepository.save(learner);
    }

    public List<LearnerDto> createLearners(List<TargetLanguageWithProficiency> data, User user, boolean replace) {
        UUID userId = user.getId();

        if (replace) {
            log.info("Replacing target languages for user {}", userId);
            List<Language> languagesToRemove = learnerRepository.findAllLanguagesByUserId(userId);

            languagesToRemove.forEach(lang -> {
                log.debug("Publishing UserRemovedTargetLanguageEvent for user {}, lang {}", userId, lang);
                eventPublisher.publishEvent(new UserRemovedTargetLanguageEvent(userId, lang));
            });

            if (!languagesToRemove.isEmpty()) {
                learnerRepository.deleteAllByUserId(userId);
                log.info("Deleted {} existing target languages for user {}", languagesToRemove.size(), userId);
            }
        }

        int currentTargetLangs = learnerRepository.countLearnersByUserId(userId);
        planValidationService.validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, currentTargetLangs + data.size());

        data.forEach(targetLanguageWithProficiency -> {
            Language code = targetLanguageWithProficiency.language();

            if (!replace && learnerRepository.existsByUserIdAndLanguage(userId, code)) {
                log.warn("User {} already has target language {}. Skipping addition.", userId, code);
                return; // Skip this language
            }

            learnerRepository.save(new Learner(user, code, targetLanguageWithProficiency.cefrLevel()));
            log.info("User {} added target language {}.", userId, code);
            log.debug("Publishing UserAddedTargetLanguageEvent for user {}, lang {}", userId, code);
            eventPublisher.publishEvent(new UserAddedTargetLanguageEvent(userId, code));
        });

        return learnerMapper.toDto(getUserWithLearners(userId).getLearners());
    }

    public void deleteLearner(Language code, UUID userId) {
        User user = getUserWithLearners(userId);

        findLearner(user, code)
                .ifPresentOrElse(
                        learner -> {
                            if (user.getLearners().size() == 1) {
                                log.warn("Learner {} has only one target language. Cannot remove it.", learner.getId());
                                throw new BadUserRequestActionException("You must have at least one target language.");
                            }

                            long activeLearners = user.getLearners().stream()
                                    .filter(Learner::isActive)
                                    .count();
                            if (learner.isActive() && activeLearners == 1) {
                                throw new BadUserRequestActionException("At least one target language must be active.");
                            }

                            cardService.deleteByLanguage(code, learner);
                            learnerRepository.delete(learner);
                            log.info("Deleted learner for language {} for user {}", code, userId);

                            eventPublisher.publishEvent(new UserRemovedTargetLanguageEvent(userId, code));
                        },
                        () -> {
                            log.warn("Language {} was not found in user {}'s target languages.", code, userId);
                            throw new BadUserRequestActionException(
                                    String.format("Language %s is not in your target languages.", code));
                        });
    }

    private Optional<Learner> findLearner(User user, Language language) {
        return user.getLearners().stream()
                .filter(learner -> learner.getLanguage().equals(language))
                .findFirst();
    }

    public User getUserWithLearners(UUID id) {
        return userRepository
                .findUserWithLearners(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }
}
