package com.almonium.user.core.service;

import com.almonium.engine.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;

public interface LearnerService {
    void updateTargetLanguages(List<Language> dto, Learner user);

    void updateFluentLanguages(List<Language> dto, Learner user);
}
