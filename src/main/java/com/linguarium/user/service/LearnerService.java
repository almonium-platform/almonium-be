package com.linguarium.user.service;

import com.linguarium.user.model.Learner;

import java.util.List;

public interface LearnerService {
    void updateTargetLanguages(List<String> dto, Learner user);

    void updateFluentLanguages(List<String> dto, Learner user);
}
