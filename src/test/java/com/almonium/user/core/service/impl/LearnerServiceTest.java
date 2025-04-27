package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.CardService;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import com.almonium.user.core.dto.response.LearnerDto;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.mapper.LearnerMapper;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.LearnerService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Example test class for the new LearnerService implementation.
 */
@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class LearnerServiceTest {

    @Mock
    LearnerRepository learnerRepository;

    @Mock
    LearnerMapper learnerMapper;

    @Mock
    PlanValidationService planValidationService;

    @Mock
    CardService cardService;

    @Mock
    UserRepository userRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    LearnerService learnerService;

    @DisplayName("Should add multiple target languages when replacing existing ones")
    @Test
    void givenUserAndMultipleLanguages_whenCreateLearnersWithReplace_thenLearnersAreDeletedAndCreated() {
        // ─── Arrange ──────────────────────────────────────────────────────────────
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .learners(List.of(Learner.builder()
                        .id(UUID.randomUUID())
                        .language(Language.EN)
                        .build()))
                .build();

        List<TargetLanguageWithProficiency> languages = List.of(
                new TargetLanguageWithProficiency(Language.FR, CEFR.A1),
                new TargetLanguageWithProficiency(Language.DE, CEFR.B1));

        // existing target languages → guarantees deleteAllByUserId() path
        when(learnerRepository.findAllLanguagesByUserId(userId)).thenReturn(List.of(Language.EN));

        when(learnerRepository.countLearnersByUserId(userId)).thenReturn(0);

        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.of(user));

        // mapper stub (method returns something, we don't care what)
        when(learnerMapper.toDto(Mockito.<List<Learner>>any())).thenReturn(Collections.emptyList());

        // ─── Act ──────────────────────────────────────────────────────────────────
        learnerService.createLearners(languages, user, true);

        // ─── Assert ───────────────────────────────────────────────────────────────
        // 1. previous learners deleted
        verify(learnerRepository).deleteAllByUserId(userId);

        // 2. plan check performed with new total (2)
        verify(planValidationService).validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, 2);

        // 3. two new learners saved with correct data
        verify(learnerRepository)
                .save(argThat(l -> l.getLanguage() == Language.FR
                        && l.getSelfReportedLevel() == CEFR.A1
                        && l.getUser().equals(user)));

        verify(learnerRepository)
                .save(argThat(l -> l.getLanguage() == Language.DE
                        && l.getSelfReportedLevel() == CEFR.B1
                        && l.getUser().equals(user)));

        // 4. mapper called
        verify(learnerMapper).toDto(Mockito.<List<Learner>>any());
    }

    @DisplayName("Should add multiple target languages without replacing existing ones")
    @Test
    void givenUserAndMultipleLanguages_whenCreateLearnersWithoutReplace_thenLearnersAreCreated() {
        // Arrange
        UUID id = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();

        User user = User.builder()
                .id(id)
                .learners(List.of(
                        Learner.builder().id(learnerId).language(Language.EN).build()))
                .build();

        List<TargetLanguageWithProficiency> languages = List.of(
                new TargetLanguageWithProficiency(Language.FR, CEFR.A1),
                new TargetLanguageWithProficiency(Language.DE, CEFR.B1));

        when(learnerRepository.countLearnersByUserId(user.getId())).thenReturn(1);
        when(userRepository.findUserWithLearners(user.getId())).thenReturn(Optional.of(user));

        // Act
        learnerService.createLearners(languages, user, false);

        // Assert

        // Validate plan feature for the new count of target languages
        verify(planValidationService).validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, 3);

        // Ensure new learners are saved with correct languages and CEFR levels
        verify(learnerRepository)
                .save(argThat(learner -> learner.getLanguage() == Language.FR
                        && learner.getSelfReportedLevel() == CEFR.A1
                        && learner.getUser().equals(user)));
        verify(learnerRepository)
                .save(argThat(learner -> learner.getLanguage() == Language.DE
                        && learner.getSelfReportedLevel() == CEFR.B1
                        && learner.getUser().equals(user)));
    }

    @DisplayName("Skips saving if target language already exists")
    @Test
    void givenExistingTargetLanguage_whenCreateLearners_thenSkipsInsert() {
        // arrange
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        List<TargetLanguageWithProficiency> languages =
                List.of(new TargetLanguageWithProficiency(Language.FR, CEFR.A1));

        when(learnerRepository.existsByUserIdAndLanguage(userId, Language.FR)).thenReturn(true);
        when(learnerRepository.countLearnersByUserId(userId)).thenReturn(1); // current total

        // stub look-up used by getUserWithLearners(…)
        User userWithLearners =
                User.builder().id(userId).learners(Collections.emptyList()).build();
        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.of(userWithLearners));

        when(learnerMapper.toDto(Mockito.<List<Learner>>any())).thenReturn(Collections.emptyList());
        // act
        List<LearnerDto> result = learnerService.createLearners(languages, user, false);

        // assert
        assertThat(result).isEmpty();
        verify(learnerRepository, never()).save(any()); // nothing saved
        verify(applicationEventPublisher, never()).publishEvent(any());
    }

    @DisplayName("Should remove target language when user has multiple learners")
    @Test
    void givenUserWithMultipleLangs_whenDeleteLearner_thenDeletesLearner() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        // The user has, say, 2 learners: EN and FR
        Learner enLearner = Learner.builder()
                .id(UUID.randomUUID())
                .user(user)
                .language(Language.EN)
                .build();
        Learner frLearner = Learner.builder()
                .id(UUID.randomUUID())
                .user(user)
                .language(Language.FR)
                .build();
        user.setLearners(List.of(enLearner, frLearner));

        // The user is retrieved with all learners
        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.of(user));

        // Act
        learnerService.deleteLearner(Language.FR, userId);

        // Assert
        // Should call cardService.deleteByLanguage(...) for the FR learner
        verify(cardService).deleteByLanguage(Language.FR, frLearner);
        // Then it should remove the FR learner
        verify(learnerRepository).delete(frLearner);
    }

    @DisplayName("Should throw exception if user tries to remove sole target language")
    @Test
    void givenUserWithSingleLearner_whenDeleteLearner_thenThrowsException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        Learner onlyLearner = Learner.builder()
                .id(UUID.randomUUID())
                .user(user)
                .language(Language.EN)
                .build();
        user.setLearners(List.of(onlyLearner));

        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> learnerService.deleteLearner(Language.EN, userId))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("You must have at least one target language");

        verify(cardService, never()).deleteByLanguage(any(), any());
        verify(learnerRepository, never()).delete(any(Learner.class));
    }

    @DisplayName("Should throw exception if user does not have that language to remove")
    @Test
    void givenUserWithoutThatLang_whenDeleteLearner_thenThrowsException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        Learner enLearner = Learner.builder()
                .id(UUID.randomUUID())
                .user(user)
                .language(Language.EN)
                .build();
        user.setLearners(List.of(enLearner));

        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> learnerService.deleteLearner(Language.FR, userId))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("Language FR is not in your target languages");

        verify(cardService, never()).deleteByLanguage(any(), any());
        verify(learnerRepository, never()).delete(any(Learner.class));
    }

    @DisplayName("Should throw EntityNotFoundException if user not found in removeTargetLanguage")
    @Test
    void givenNonExistentUserId_whenDeleteLearner_thenThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(userRepository.findUserWithLearners(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> learnerService.deleteLearner(Language.EN, id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found: ");
    }
}
