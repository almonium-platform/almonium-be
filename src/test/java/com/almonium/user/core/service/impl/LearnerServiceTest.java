package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
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
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.LearnerService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Example test class for the new LearnerService implementation.
 */
@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class LearnerServiceTest {

    @Mock
    LearnerRepository learnerRepository;

    @Mock
    PlanValidationService planValidationService;

    @Mock
    CardService cardService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    LearnerService learnerService;

    @DisplayName("Should add multiple target languages when replacing existing ones")
    @Test
    void givenUserAndMultipleLanguages_whenAddTargetLanguagesWithReplace_thenLearnersAreCreated() {
        // Arrange
        User user = User.builder().id(10L).build();
        List<TargetLanguageWithProficiency> languages = List.of(
                new TargetLanguageWithProficiency(CEFR.A1, Language.FR),
                new TargetLanguageWithProficiency(CEFR.B1, Language.DE));

        when(learnerRepository.countLearnersByUserId(user.getId())).thenReturn(0);

        // Act
        learnerService.addTargetLanguages(languages, user, true);

        // Assert
        verify(learnerRepository).deleteAllByUserId(user.getId());
        verify(planValidationService).validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, 2);
        verify(learnerRepository)
                .save(argThat(learner -> learner.getLanguage() == Language.FR
                        && learner.getUser().equals(user)));
        verify(learnerRepository)
                .save(argThat(learner -> learner.getLanguage() == Language.DE
                        && learner.getUser().equals(user)));
    }

    @DisplayName("Should add multiple target languages without replacing existing ones")
    @Test
    void givenUserAndMultipleLanguages_whenAddTargetLanguagesWithoutReplace_thenLearnersAreCreated() {
        // Arrange
        User user = User.builder().id(10L).build();
        List<TargetLanguageWithProficiency> languages = List.of(
                new TargetLanguageWithProficiency(CEFR.A1, Language.FR),
                new TargetLanguageWithProficiency(CEFR.B1, Language.DE));

        when(learnerRepository.countLearnersByUserId(user.getId())).thenReturn(1);

        // Act
        learnerService.addTargetLanguages(languages, user, false);

        // Assert
        verify(learnerRepository, never()).deleteAllByUserId(user.getId());
        verify(planValidationService).validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, 3);
        verify(learnerRepository)
                .save(argThat(learner -> learner.getLanguage() == Language.FR
                        && learner.getUser().equals(user)));
        verify(learnerRepository)
                .save(argThat(learner -> learner.getLanguage() == Language.DE
                        && learner.getUser().equals(user)));
    }

    @DisplayName("Should throw exception if a target language already exists")
    @Test
    void givenExistingTargetLanguage_whenAddTargetLanguages_thenThrowsException() {
        // Arrange
        User user = User.builder().id(10L).build();
        List<TargetLanguageWithProficiency> languages =
                List.of(new TargetLanguageWithProficiency(CEFR.A1, Language.FR));

        when(learnerRepository.findByUserIdAndLanguage(user.getId(), Language.FR))
                .thenReturn(Optional.of(
                        Learner.builder().user(user).language(Language.FR).build()));

        // Act & Assert
        assertThatThrownBy(() -> learnerService.addTargetLanguages(languages, user, false))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("You already have this target language");
        verify(learnerRepository, never()).save(any(Learner.class));
    }

    @DisplayName("Should replace all existing target languages")
    @Test
    void givenReplaceFlag_whenAddTargetLanguages_thenDeletesAndAddsLearners() {
        // Arrange
        User user = User.builder().id(10L).build();
        List<TargetLanguageWithProficiency> languages = List.of(
                new TargetLanguageWithProficiency(CEFR.A1, Language.FR),
                new TargetLanguageWithProficiency(CEFR.B2, Language.DE));

        // Act
        learnerService.addTargetLanguages(languages, user, true);

        // Assert
        verify(learnerRepository).deleteAllByUserId(user.getId());
        verify(planValidationService).validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, 2);
        verify(learnerRepository).save(argThat(learner -> learner.getLanguage() == Language.FR));
        verify(learnerRepository).save(argThat(learner -> learner.getLanguage() == Language.DE));
    }

    @DisplayName("Should remove target language when user has multiple learners")
    @Test
    void givenUserWithMultipleLangs_whenRemoveTargetLanguage_thenDeletesLearner() {
        // Arrange
        long userId = 20L;
        User user = User.builder().id(userId).build();

        // The user has, say, 2 learners: EN and FR
        Learner enLearner =
                Learner.builder().id(100L).user(user).language(Language.EN).build();
        Learner frLearner =
                Learner.builder().id(101L).user(user).language(Language.FR).build();
        user.setLearners(List.of(enLearner, frLearner));

        // The user is retrieved with all learners
        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.of(user));

        // Act
        learnerService.removeTargetLanguage(Language.FR, userId);

        // Assert
        // Should call cardService.deleteByLanguage(...) for the FR learner
        verify(cardService).deleteByLanguage(Language.FR, frLearner);
        // Then it should remove the FR learner
        verify(learnerRepository).delete(frLearner);
    }

    @DisplayName("Should throw exception if user tries to remove sole target language")
    @Test
    void givenUserWithSingleLearner_whenRemoveTargetLanguage_thenThrowsException() {
        // Arrange
        long userId = 21L;
        User user = User.builder().id(userId).build();

        Learner onlyLearner =
                Learner.builder().id(200L).user(user).language(Language.EN).build();
        user.setLearners(List.of(onlyLearner));

        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> learnerService.removeTargetLanguage(Language.EN, userId))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("You must have at least one target language");

        verify(cardService, never()).deleteByLanguage(any(), any());
        verify(learnerRepository, never()).delete(any(Learner.class));
    }

    @DisplayName("Should throw exception if user does not have that language to remove")
    @Test
    void givenUserWithoutThatLang_whenRemoveTargetLanguage_thenThrowsException() {
        // Arrange
        long userId = 22L;
        User user = User.builder().id(userId).build();

        Learner enLearner =
                Learner.builder().id(300L).user(user).language(Language.EN).build();
        user.setLearners(List.of(enLearner));

        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThatThrownBy(() -> learnerService.removeTargetLanguage(Language.FR, userId))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("Language FR is not in your target languages");

        verify(cardService, never()).deleteByLanguage(any(), any());
        verify(learnerRepository, never()).delete(any(Learner.class));
    }

    @DisplayName("Should throw EntityNotFoundException if user not found in removeTargetLanguage")
    @Test
    void givenNonExistentUserId_whenRemoveTargetLanguage_thenThrowException() {
        // Arrange
        long userId = 999L;
        when(userRepository.findUserWithLearners(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> learnerService.removeTargetLanguage(Language.EN, userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found: 999");
    }
}
