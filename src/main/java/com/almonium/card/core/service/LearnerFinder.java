package com.almonium.card.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerFinder {
    LearnerRepository learnerRepository;

    public Learner findLearner(User user, Language language) {
        return learnerRepository
                .findByUserIdAndLanguage(user.getId(), language)
                .orElseThrow(() -> new EntityNotFoundException("Learner not found for language: " + language));
    }

    /** The learner a new word may go to. A set-aside language keeps everything it has and takes nothing new. */
    public Learner findActiveLearner(User user, Language language) {
        Learner learner = findLearner(user, language);
        requireActive(learner);
        return learner;
    }

    /**
     * The rule the interface only draws: a set-aside language is read-only. Enforced where the write happens, so it
     * holds for every caller rather than only for the one that hides the button.
     */
    public void requireActive(Learner learner) {
        if (!learner.isActive()) {
            throw new BadUserRequestActionException(
                    learner.getLanguage() + " is set aside. Make it active again to add words to it.");
        }
    }
}
