package com.linguarium.user.core.service;

import com.linguarium.engine.translator.model.Language;
import com.linguarium.user.core.model.Learner;
import java.util.List;

public interface LearnerService {
    void updateTargetLanguages(List<Language> dto, Learner user);

    void updateFluentLanguages(List<Language> dto, Learner user);
}
