package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.CardService;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerService {
    LearnerRepository learnerRepository;
    PlanValidationService planValidationService;
    CardService cardService;
    UserRepository userRepository;

    public void addTargetLanguages(List<TargetLanguageWithProficiency> data, User user, boolean replace) {
        if (replace) {
            learnerRepository.deleteAllByUserId(user.getId());
        }
        long userId = user.getId();
        int currentTargetLangs = learnerRepository.countLearnersByUserId(userId);
        planValidationService.validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, currentTargetLangs + data.size());
        data.forEach(targetLanguageWithProficiency -> {
            Language code = targetLanguageWithProficiency.language();
            learnerRepository.findByUserIdAndLanguage(userId, code).ifPresent(existingLearner -> {
                log.warn("User {} already has target language {}.", userId, code);
                throw new BadUserRequestActionException("You already have this target language.");
            });

            learnerRepository.save(new Learner(user, code, targetLanguageWithProficiency.cefrLevel()));
            log.info("User {} added target language {}.", userId, code);
        });
    }

    public void removeTargetLanguage(Language code, long userId) {
        var user = getUserWithLearners(userId);

        findLearner(user, code)
                .ifPresentOrElse(
                        learner -> {
                            if (user.getLearners().size() == 1) {
                                log.warn("Learner {} has only one target language. Cannot remove it.", learner.getId());
                                throw new BadUserRequestActionException("You must have at least one target language.");
                            }

                            cardService.deleteByLanguage(code, learner);
                            learnerRepository.delete(learner);
                        },
                        () -> {
                            log.warn("Language {} was not found in user {}'s target languages.", code, userId);
                            throw new BadUserRequestActionException(
                                    String.format("Language %s is not in your target languages.", code));
                        });
    }

    public Optional<Learner> findLearner(User user, Language language) {
        return user.getLearners().stream()
                .filter(learner -> learner.getLanguage().equals(language))
                .findFirst();
    }

    public User getUserWithLearners(long id) {
        return userRepository
                .findUserWithLearners(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }
}
