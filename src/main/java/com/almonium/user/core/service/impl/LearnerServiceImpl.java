package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.CardService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.service.LearnerService;
import java.util.HashSet;
import java.util.Set;
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
public class LearnerServiceImpl implements LearnerService {
    LearnerRepository learnerRepository;
    CardService cardService;

    @Override
    public void setupLanguages(Set<Language> nativeLangs, Set<Language> targetLangs, Learner user) {
        user.setFluentLangs(new HashSet<>(nativeLangs));
        user.setTargetLangs(new HashSet<>(targetLangs));
        learnerRepository.save(user);
    }

    @Override
    public void addTargetLanguage(Language code, long learnerId) {
        var learner = getLearnerWithTargetLangs(learnerId);
        learner.getTargetLangs().add(code);
        learnerRepository.save(learner);
    }

    @Override
    public void removeTargetLanguage(Language code, long learnerId) {
        var learner = getLearnerWithTargetLangs(learnerId);

        if (learner.getTargetLangs().size() == 1) {
            log.warn("Learner {} has only one target language. Cannot remove it.", learnerId);
            throw new BadUserRequestActionException("You must have at least one target language.");
        }

        boolean removed = learner.getTargetLangs().remove(code);
        if (!removed) {
            log.warn("Language {} was not found in learner {}'s target languages.", code, learnerId);
            throw new BadUserRequestActionException(
                    String.format("Language %s is not in your target languages.", code));
        }
        // TODO later more logic will be here. Delete everything related to this language.
        cardService.deleteByLanguage(code, learner);
        learnerRepository.save(learner);
    }

    @Override
    public void updateFluentLanguages(Set<Language> langs, Learner learner) {
        learner.setFluentLangs(new HashSet<>(langs));
        learnerRepository.save(learner);
    }

    private Learner getLearnerWithTargetLangs(long id) {
        return learnerRepository.findLearnerWithTargetLangs(id).orElseThrow();
    }
}
