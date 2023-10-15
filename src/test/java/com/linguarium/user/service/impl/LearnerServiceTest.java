package com.linguarium.user.service.impl;

import com.linguarium.translator.model.Language;
import com.linguarium.user.dto.LangCodeDto;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.LearnerRepository;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LearnerServiceTest {
    @InjectMocks
    LearnerServiceImpl learnerService;

    @Mock
    LearnerRepository learnerRepository;

    @BeforeEach
    void setUp() {
        learnerService = new LearnerServiceImpl(learnerRepository);
    }

    @Test
    @DisplayName("Should set new fluent languages for user with existing target languages")
    void givenUserWithExistingTargetLanguages_whenSetTargetLangs_thenNewLanguagesSet() {
        LangCodeDto dto = new LangCodeDto(new String[]{"DE", "FR", "ES"});
        Learner learner = new Learner();
        learner.setTargetLangs(Set.of("EN", "DE", "FR"));

        learnerService.setTargetLangs(dto, learner);

        assertThat(learner.getTargetLangs())
                .as("The new target languages should be set correctly")
                .containsExactlyInAnyOrder("DE", "FR", "ES");

        verify(learnerRepository).save(learner);
    }

    @Test
    @DisplayName("Should set target languages for user based on language code DTO")
    void givenLangCodeDto_whenSetTargetLangs_thenUserTargetLanguagesAreSet() {
        LangCodeDto dto = new LangCodeDto();
        dto.setCodes(new String[]{"EN", "DE"});

        Learner learner = new Learner();

        learnerService.setTargetLangs(dto, learner);

        assertThat(learner.getTargetLangs()).hasSize(2).containsExactlyInAnyOrder("EN", "DE");
        verify(learnerRepository, times(1)).save(learner);
    }

    @Test
    @DisplayName("Should set new fluent languages for user with existing fluent languages")
    void givenUserWithExistingFluentLanguages_whenSetFluentLangs_thenNewLanguagesSet() {
        LangCodeDto dto = new LangCodeDto(new String[]{"DE", "FR", "ES"});
        Learner learner = new Learner();
        learner.setFluentLangs(new HashSet<>(Arrays.asList("EN", "DE", "FR")));

        learnerService.setFluentLangs(dto, learner);

        assertThat(learner.getFluentLangs())
                .as("The new fluent languages should be set correctly")
                .hasSize(dto.getCodes().length)
                .containsExactlyInAnyOrder("DE", "FR", "ES");

        verify(learnerRepository).save(learner);
    }

    @Test
    @DisplayName("Should set fluent languages for user based on language code DTO")
    void givenLangCodeDto_whenSetFluentLangs_thenUserFluentLanguagesAreSet() {
        LangCodeDto dto = new LangCodeDto();
        dto.setCodes(new String[]{"EN", "DE"});

        Learner learner = new Learner();

        learnerService.setFluentLangs(dto, learner);

        assertThat(learner.getFluentLangs()).hasSize(2).containsExactlyInAnyOrder("EN", "DE");
        verify(learnerRepository, times(1)).save(learner);
    }

    @NotNull
    static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("password");
        user.setEmail("john@example.com");
        Profile profile = new Profile();
        profile.setUiLang(Language.EN);
        profile.setProfilePicLink("profile.jpg");
        profile.setBackground("background.jpg");
        profile.setStreak(5);
        user.setProfile(profile);
        Learner learner = new Learner();
        learner.setTargetLangs(Set.of(
                Language.DE.name(),
                Language.FR.name()
        ));
        learner.setFluentLangs(Set.of(
                Language.ES.name(),
                Language.RU.name()
        ));
        user.setLearner(learner);
        return user;
    }
}
