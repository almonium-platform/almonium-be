package com.almonium.card.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
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
}
