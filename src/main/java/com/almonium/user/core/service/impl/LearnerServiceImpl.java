package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.engine.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.service.LearnerService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerServiceImpl implements LearnerService {
    LearnerRepository learnerRepository;

    @Override
    public void setupLanguages(Set<Language> nativeLangs, Set<Language> targetLangs, Learner user) {
        user.setFluentLangs(new HashSet<>(nativeLangs));
        user.setTargetLangs(new HashSet<>(targetLangs));
        learnerRepository.save(user);
    }

    @Override
    public void updateTargetLanguages(Set<Language> langs, Learner learner) {
        learner.setTargetLangs(new HashSet<>(langs));
        learnerRepository.save(learner);
    }

    @Override
    public void updateFluentLanguages(Set<Language> langs, Learner learner) {
        learner.setFluentLangs(new HashSet<>(langs));
        learnerRepository.save(learner);
    }
}
