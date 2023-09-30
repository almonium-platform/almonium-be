package com.linguarium.user.service.impl;

import com.linguarium.card.repository.CardTagRepository;
import com.linguarium.card.repository.TagRepository;
import com.linguarium.translator.model.Language;
import com.linguarium.translator.model.LanguageEntity;
import com.linguarium.translator.repository.LanguageRepository;
import com.linguarium.user.dto.LangCodeDto;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.LearnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LearnerServiceTest {
    @InjectMocks
    LearnerServiceImpl learnerService;

    @Mock
    CardTagRepository cardTagRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    LanguageRepository languageRepository;
    @Mock
    LearnerRepository learnerRepository;

    @BeforeEach
    void setUp() {
        learnerService = new LearnerServiceImpl(learnerRepository, cardTagRepository, tagRepository,languageRepository);
    }

    @Test
    @DisplayName("Should set new fluent languages for user with existing target languages")
    void givenUserWithExistingTargetLanguages_whenSetTargetLangs_thenNewLanguagesSet() {
        LangCodeDto dto = new LangCodeDto(new String[]{"DE", "FR", "ES"});
        Learner learner = new Learner();
        Set<LanguageEntity> existingLanguages = Set.of(
                new LanguageEntity(1L, Language.ENGLISH),
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH)
        );
        learner.setTargetLangs(existingLanguages);

        Set<LanguageEntity> mockedLanguages = Set.of(
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH),
                new LanguageEntity(4L, Language.SPANISH)
        );

        when(languageRepository.findByCode(any(Language.class)))
                .thenAnswer(invocation -> {
                    Language code = invocation.getArgument(0);
                    return mockedLanguages.stream()
                            .filter(lang -> lang.getCode() == code)
                            .findFirst();
                });

        learnerService.setTargetLangs(dto, learner);

        Set<LanguageEntity> updatedLanguages = learner.getTargetLangs();
        assertThat(updatedLanguages)
                .as("The new target languages should be set correctly")
                .hasSize(dto.getCodes().length)
                .extracting(LanguageEntity::getCode)
                .containsExactlyInAnyOrder(Language.GERMAN, Language.FRENCH, Language.SPANISH);

        verify(languageRepository, times(3)).findByCode(any(Language.class));
        verify(learnerRepository).save(learner);
    }

    @Test
    @DisplayName("Should set target languages for user based on language code DTO")
    void givenLangCodeDto_whenSetTargetLangs_thenUserTargetLanguagesAreSet() {
        LangCodeDto dto = new LangCodeDto();
        dto.setCodes(new String[]{"EN", "DE"});

        Learner learner = new Learner();
        Set<LanguageEntity> languages = new HashSet<>();
        when(languageRepository.findByCode(any(Language.class))).thenAnswer((Answer<Optional<LanguageEntity>>) invocation -> {
            Language code = invocation.getArgument(0);
            LanguageEntity languageEntity = new LanguageEntity();
            languageEntity.setCode(code);
            languages.add(languageEntity);
            return Optional.of(languageEntity);
        });

        learnerService.setTargetLangs(dto, learner);

        verify(languageRepository, times(2)).findByCode(any(Language.class));
        assertThat(learner.getTargetLangs()).hasSize(2).containsExactlyInAnyOrderElementsOf(languages);
        verify(learnerRepository, times(1)).save(learner);
    }

    @Test
    @DisplayName("Should set new fluent languages for user with existing fluent languages")
    void givenUserWithExistingFluentLanguages_whenSetFluentLangs_thenNewLanguagesSet() {
        LangCodeDto dto = new LangCodeDto(new String[]{"DE", "FR", "ES"});
        Learner learner = new Learner();
        Set<LanguageEntity> existingLanguages = Set.of(
                new LanguageEntity(1L, Language.ENGLISH),
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH)
        );
        learner.setFluentLangs(existingLanguages);
        Set<LanguageEntity> mockedLanguages = Set.of(
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH),
                new LanguageEntity(4L, Language.SPANISH)
        );

        when(languageRepository.findByCode(any(Language.class)))
                .thenAnswer(invocation -> {
                    Language code = invocation.getArgument(0);
                    return mockedLanguages.stream()
                            .filter(lang -> lang.getCode() == code)
                            .findFirst();
                });

        learnerService.setFluentLangs(dto, learner);

        Set<LanguageEntity> updatedLanguages = learner.getFluentLangs();
        assertThat(updatedLanguages)
                .as("The new fluent languages should be set correctly")
                .hasSize(dto.getCodes().length)
                .extracting(LanguageEntity::getCode)
                .containsExactlyInAnyOrder(Language.GERMAN, Language.FRENCH, Language.SPANISH);

        verify(languageRepository, times(3)).findByCode(any(Language.class));
        verify(learnerRepository).save(learner);
    }

    @Test
    @DisplayName("Should set fluent languages for user based on language code DTO")
    void givenLangCodeDto_whenSetFluentLangs_thenUserFluentLanguagesAreSet() {
        LangCodeDto dto = new LangCodeDto();
        dto.setCodes(new String[]{"EN", "DE"});

        Learner learner = new Learner();

        Set<LanguageEntity> languages = new HashSet<>();
        when(languageRepository.findByCode(any(Language.class))).thenAnswer((Answer<Optional<LanguageEntity>>) invocation -> {
            Language code = invocation.getArgument(0);
            LanguageEntity languageEntity = new LanguageEntity();
            languageEntity.setCode(code);
            languages.add(languageEntity);
            return Optional.of(languageEntity);
        });

        learnerService.setFluentLangs(dto, learner);

        verify(languageRepository, times(2)).findByCode(any(Language.class));
        assertThat(learner.getFluentLangs()).hasSize(2).containsExactlyInAnyOrderElementsOf(languages);
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
        profile.setUiLang(Language.ENGLISH);
        profile.setProfilePicLink("profile.jpg");
        profile.setBackground("background.jpg");
        profile.setStreak(5);
        user.setProfile(profile);
        Learner learner = new Learner();
        learner.setTargetLangs(Set.of(
                new LanguageEntity(2L, Language.GERMAN),
                new LanguageEntity(3L, Language.FRENCH)
        ));
        learner.setFluentLangs(Set.of(
                new LanguageEntity(4L, Language.SPANISH),
                new LanguageEntity(5L, Language.RUSSIAN)
        ));
        user.setLearner(learner);
        return user;
    }
}
