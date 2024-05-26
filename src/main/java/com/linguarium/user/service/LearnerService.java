package com.linguarium.user.service;

import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import java.util.List;

public interface LearnerService {
    void updateTargetLanguages(List<Language> dto, Learner user);

    void updateFluentLanguages(List<Language> dto, Learner user);
}
