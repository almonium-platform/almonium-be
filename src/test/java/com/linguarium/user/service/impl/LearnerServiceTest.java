package com.linguarium.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.user.repository.LearnerRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class LearnerServiceTest {
    @InjectMocks
    LearnerServiceImpl learnerService;

    @Mock
    LearnerRepository learnerRepository;

    @DisplayName("Should set new fluent languages for user with existing target languages")
    @Test
    void givenUserWithExistingTargetLanguages_whenSetTargetLangs_thenNewLanguagesSet() {
        List<String> langCodes = List.of(Language.DE.name(), Language.FR.name(), Language.ES.name());
        Learner learner = new Learner();
        learner.setTargetLangs(Set.of(Language.EN.name(), Language.DE.name(), Language.FR.name()));

        learnerService.updateTargetLanguages(langCodes, learner);

        assertThat(learner.getTargetLangs())
                .as("The new target languages should be set correctly")
                .containsExactlyInAnyOrder(Language.DE.name(), Language.FR.name(), Language.ES.name());

        verify(learnerRepository).save(learner);
    }

    @DisplayName("Should set target languages for user based on language code DTO")
    @Test
    void givenLangCodeDto_whenSetTargetLangs_thenUserTargetLanguagesAreSet() {
        List<String> langCodes = List.of(Language.DE.name(), Language.EN.name());

        Learner learner = new Learner();

        learnerService.updateTargetLanguages(langCodes, learner);

        assertThat(learner.getTargetLangs())
                .hasSize(2)
                .containsExactlyInAnyOrder(Language.EN.name(), Language.DE.name());
        verify(learnerRepository, times(1)).save(learner);
    }

    @DisplayName("Should set new fluent languages for user with existing fluent languages")
    @Test
    void givenUserWithExistingFluentLanguages_whenSetFluentLangs_thenNewLanguagesSet() {
        List<String> langCodes = List.of(Language.DE.name(), Language.FR.name(), Language.ES.name());
        Learner learner = new Learner();
        learner.setFluentLangs(
                new HashSet<>(Arrays.asList(Language.EN.name(), Language.DE.name(), Language.FR.name())));

        learnerService.updateFluentLanguages(langCodes, learner);

        assertThat(learner.getFluentLangs())
                .as("The new fluent languages should be set correctly")
                .hasSize(langCodes.size())
                .containsExactlyInAnyOrder(Language.DE.name(), Language.FR.name(), Language.ES.name());

        verify(learnerRepository).save(learner);
    }

    @DisplayName("Should set fluent languages for user based on language code DTO")
    @Test
    void givenLangCodeDto_whenSetFluentLangs_thenUserFluentLanguagesAreSet() {
        List<String> langCodes = List.of(Language.DE.name(), Language.EN.name());

        Learner learner = new Learner();

        learnerService.updateFluentLanguages(langCodes, learner);

        assertThat(learner.getFluentLangs())
                .hasSize(2)
                .containsExactlyInAnyOrder(Language.EN.name(), Language.DE.name());
        verify(learnerRepository, times(1)).save(learner);
    }
}
