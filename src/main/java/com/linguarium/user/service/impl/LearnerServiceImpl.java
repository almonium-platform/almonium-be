package com.linguarium.user.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.user.model.Learner;
import com.linguarium.user.repository.LearnerRepository;
import com.linguarium.user.service.LearnerService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerServiceImpl implements LearnerService {
    LearnerRepository learnerRepository;

    @Override
    @Transactional
    public void updateTargetLanguages(List<String> langs, Learner learner) {
        Set<String> languages = new HashSet<>(langs);
        learner.setTargetLangs(languages);
        learnerRepository.save(learner);
    }

    @Override
    @Transactional
    public void updateFluentLanguages(List<String> langs, Learner learner) {
        Set<String> languages = new HashSet<>(langs);
        learner.setFluentLangs(languages);
        learnerRepository.save(learner);
    }
}
