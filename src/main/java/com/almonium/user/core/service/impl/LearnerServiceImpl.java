package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.engine.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.service.LearnerService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerServiceImpl implements LearnerService {
    LearnerRepository learnerRepository;

    @Override
    @Transactional
    public void updateTargetLanguages(List<Language> langs, Learner learner) {
        Set<Language> languages = new HashSet<>(langs);
        learner.setTargetLangs(languages);
        learnerRepository.save(learner);
    }

    @Override
    @Transactional
    public void updateFluentLanguages(List<Language> langs, Learner learner) {
        Set<Language> languages = new HashSet<>(langs);
        learner.setFluentLangs(languages);
        learnerRepository.save(learner);
    }
}
