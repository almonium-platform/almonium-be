package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.dto.InterestDto;
import com.almonium.user.core.dto.LanguageSetupRequest;
import com.almonium.user.core.dto.LearnerDto;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.mapper.InterestMapper;
import com.almonium.user.core.mapper.LearnerMapper;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.core.repository.InterestRepository;
import com.almonium.user.core.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class OnboardingService {
    LearnerService learnerService;
    UserService userService;

    InterestRepository interestRepository;
    UserRepository userRepository;

    InterestMapper interestMapper;
    LearnerMapper learnerMapper;

    public List<InterestDto> getInterests() {
        return interestMapper.toDto(interestRepository.findAll());
    }

    public void saveInterests(User user, List<Long> interests) {
        processStep(user, SetupStep.INTERESTS, interests, data -> userService.updateInterests(user, data));
    }

    public List<LearnerDto> setupLanguages(User user, LanguageSetupRequest request) {
        processStep(user, SetupStep.LANGUAGES, request, data -> {
            data.targetLangsData().forEach(learnerData -> learnerService.addTargetLanguage(learnerData, user));
            user.setFluentLangs(new HashSet<>(data.fluentLangs()));
        });

        return learnerMapper.toDto(learnerService.getUserWithLearners(user.getId()).getLearners());
    }

    public void completeSimpleStep(User user, SetupStep step) {
        processStep(user, step, () -> {});
    }

    private <T> void processStep(User user, SetupStep step, T data, Consumer<T> action) {
        validateActionAllowed(user, step);
        action.accept(data);
        goToNextStepIfNeededAndSaveUser(user, step);
    }

    private void processStep(User user, SetupStep step, Runnable action) {
        processStep(user, step, null, ignore -> action.run());
    }

    private void validateActionAllowed(User user, SetupStep step) {
        if (step == SetupStep.COMPLETED) {
            throw new BadUserRequestActionException("User has already completed the onboarding");
        }

        if (step.isUnreachable(user.getSetupStep())) {
            throw new BadUserRequestActionException("User not at the required step");
        }

        log.info("Action for step {} is available to user {}", step, user.getEmail());
    }

    private void goToNextStepIfNeededAndSaveUser(User user, SetupStep step) {
        if (step != user.getSetupStep()) {
            log.info("User {} is already further along than step {}", user.getEmail(), step);
            return;
        }

        SetupStep nextStep = user.getSetupStep().nextStep();
        user.setSetupStep(nextStep);
        userRepository.save(user);
        log.info("User {} has moved to step {}", user.getEmail(), nextStep);
    }
}
