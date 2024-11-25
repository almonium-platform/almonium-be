package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class LearnerServiceTest {
    @InjectMocks
    LearnerServiceImpl learnerService;

    @Mock
    LearnerRepository learnerRepository;

    @Mock
    PlanValidationService planValidationService;

    @DisplayName("Should add a new target language for user")
    @Test
    void givenLangCode_whenAddTargetLang_thenNewLangIsAdded() {
        Language langCode = Language.EN;
        long learnerId = 1L;
        Learner learner = Learner.builder()
                .user(User.builder().id(learnerId).build())
                .targetLangs(new HashSet<>(Arrays.asList(Language.EN, Language.DE)))
                .build();

        when(learnerRepository.findLearnerWithTargetLangs(learnerId)).thenReturn(Optional.of(learner));

        learnerService.addTargetLanguage(langCode, learnerId);

        assertThat(learner.getTargetLangs()).hasSize(2).containsExactlyInAnyOrder(Language.EN, Language.DE);
        verify(learnerRepository).save(learner);
        verify(learnerRepository).findLearnerWithTargetLangs(learnerId);
    }

    @DisplayName("Should set new fluent languages for user with existing fluent languages")
    @Test
    void givenUserWithExistingFluentLanguages_whenSetFluentLangs_thenNewLanguagesSet() {
        Set<Language> langCodes = Set.of(Language.DE, Language.FR, Language.ES);
        Learner learner = new Learner();
        learner.setFluentLangs(new HashSet<>(Arrays.asList(Language.EN, Language.DE, Language.FR)));

        learnerService.updateFluentLanguages(langCodes, learner);

        assertThat(learner.getFluentLangs())
                .as("The new fluent languages should be set correctly")
                .hasSize(langCodes.size())
                .containsExactlyInAnyOrder(Language.DE, Language.FR, Language.ES);

        verify(learnerRepository).save(learner);
    }

    @DisplayName("Should set fluent languages for user based on language code DTO")
    @Test
    void givenLangCodeDto_whenSetFluentLangs_thenUserFluentLanguagesAreSet() {
        Set<Language> langCodes = Set.of(Language.DE, Language.EN);

        Learner learner = new Learner();

        learnerService.updateFluentLanguages(langCodes, learner);

        assertThat(learner.getFluentLangs()).hasSize(2).containsExactlyInAnyOrder(Language.EN, Language.DE);
        verify(learnerRepository, times(1)).save(learner);
    }
}
