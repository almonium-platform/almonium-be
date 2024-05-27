package linguarium.user.core.service;

import java.util.List;
import linguarium.engine.translator.model.Language;
import linguarium.user.core.model.Learner;

public interface LearnerService {
    void updateTargetLanguages(List<Language> dto, Learner user);

    void updateFluentLanguages(List<Language> dto, Learner user);
}
