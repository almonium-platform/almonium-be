package com.linguarium.user.service;

import com.linguarium.card.model.Tag;
import com.linguarium.user.dto.LangCodeDto;
import com.linguarium.user.model.Learner;

public interface LearnerService {
    void renameTagForUser(Learner user, Tag tag, String proposedName);

    void setTargetLangs(LangCodeDto dto, Learner user);

    void setFluentLangs(LangCodeDto dto, Learner user);
}
