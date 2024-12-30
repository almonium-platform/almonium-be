package com.almonium.card.core.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LearnerFinder {
    public Learner findLearner(User user, Language language) {
        return user.getLearners().stream()
                .filter(learner -> learner.getLanguage().equals(language))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Learner not found for language: " + language));
    }
}
