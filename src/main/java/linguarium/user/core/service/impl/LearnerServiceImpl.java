package linguarium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import linguarium.engine.translator.model.enums.Language;
import linguarium.user.core.model.entity.Learner;
import linguarium.user.core.repository.LearnerRepository;
import linguarium.user.core.service.LearnerService;
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
