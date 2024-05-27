package linguarium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import linguarium.engine.translator.model.enums.Language;
import linguarium.user.core.model.entity.Learner;
import linguarium.user.core.repository.LearnerRepository;
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

    @DisplayName("Should set new fluent languages for user with existing target languages")
    @Test
    void givenUserWithExistingTargetLanguages_whenSetTargetLangs_thenNewLanguagesSet() {
        List<Language> langCodes = List.of(Language.DE, Language.FR, Language.ES);
        Learner learner = new Learner();
        learner.setTargetLangs(Set.of(Language.EN, Language.DE, Language.FR));

        learnerService.updateTargetLanguages(langCodes, learner);

        assertThat(learner.getTargetLangs())
                .as("The new target languages should be set correctly")
                .containsExactlyInAnyOrder(Language.DE, Language.FR, Language.ES);

        verify(learnerRepository).save(learner);
    }

    @DisplayName("Should set target languages for user based on language code DTO")
    @Test
    void givenLangCodeDto_whenSetTargetLangs_thenUserTargetLanguagesAreSet() {
        List<Language> langCodes = List.of(Language.DE, Language.EN);

        Learner learner = new Learner();

        learnerService.updateTargetLanguages(langCodes, learner);

        assertThat(learner.getTargetLangs()).hasSize(2).containsExactlyInAnyOrder(Language.EN, Language.DE);
        verify(learnerRepository, times(1)).save(learner);
    }

    @DisplayName("Should set new fluent languages for user with existing fluent languages")
    @Test
    void givenUserWithExistingFluentLanguages_whenSetFluentLangs_thenNewLanguagesSet() {
        List<Language> langCodes = List.of(Language.DE, Language.FR, Language.ES);
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
        List<Language> langCodes = List.of(Language.DE, Language.EN);

        Learner learner = new Learner();

        learnerService.updateFluentLanguages(langCodes, learner);

        assertThat(learner.getFluentLangs()).hasSize(2).containsExactlyInAnyOrder(Language.EN, Language.DE);
        verify(learnerRepository, times(1)).save(learner);
    }
}
