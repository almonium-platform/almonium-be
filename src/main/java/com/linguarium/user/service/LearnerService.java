package com.linguarium.user.service;

import com.linguarium.user.dto.LangCodeDto;
import com.linguarium.user.model.Learner;

public interface LearnerService {
    void setTargetLangs(LangCodeDto dto, Learner user);

    void setFluentLangs(LangCodeDto dto, Learner user);
}
