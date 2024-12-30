package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
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

    @DisplayName("Should add a new target language when user does not already have it")
    @Test
    void givenUserAndLanguageData_whenAddTargetLanguage_thenLearnerIsCreated() {
        // Arrange
        User user = User.builder().id(10L).build();
        // Suppose we want to add French with some CEFR level
        TargetLanguageWithProficiency languageData =
                new TargetLanguageWithProficiency(CEFR.A1, Language.FR);

        // No existing Learner with (userId=10, language=FR)
        when(learnerRepository.findByUserIdAndLanguage(user.getId(), Language.FR))
                .thenReturn(Optional.empty());

        // Suppose user already has 1 target language, so we pass '1' to planValidationService
        when(learnerRepository.countLearnersByUserId(10L)).thenReturn(1);

        // Act
        learnerService.addTargetLanguage(languageData, user);

        // Assert
        verify(planValidationService)
                .validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, /* new total */ 2);
        // We expect a new Learner to be saved
        verify(learnerRepository).save(argThat(learner ->
                learner.getLanguage().equals(Language.FR)
                        && learner.getUser().equals(user)));
    }

    @DisplayName("Should throw exception if user already has that target language")
    @Test
    void givenExistingTargetLanguage_whenAddTargetLanguage_thenThrowsException() {
        // Arrange
        User user = User.builder().id(11L).build();
        TargetLanguageWithProficiency languageData =
                new TargetLanguageWithProficiency(CEFR.C2, Language.DE);

        // Mock that user already has a Learner for DE
        Learner existingLearner = Learner.builder().user(user).language(Language.DE).build();
        when(learnerRepository.findByUserIdAndLanguage(11L, Language.DE))
                .thenReturn(Optional.of(existingLearner));

        // Act & Assert
        assertThatThrownBy(() -> learnerService.addTargetLanguage(languageData, user))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("You already have this target language");
        verify(planValidationService, never()).validatePlanFeature(any(), any(), anyInt());
        verify(learnerRepository, never()).save(any(Learner.class));
    }

    @DisplayName("Should remove target language when user has multiple learners")
    @Test
    void givenUserWithMultipleLangs_whenRemoveTargetLanguage_thenDeletesLearner() {
        // Arrange
        long userId = 20L;
        User user = User.builder().id(userId).build();

        // The user has, say, 2 learners: EN and FR
        Learner enLearner = Learner.builder().id(100L).user(user).language(Language.EN).build();
        Learner frLearner = Learner.builder().id(101L).user(user).language(Language.FR).build();
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

        Learner onlyLearner = Learner.builder().id(200L).user(user).language(Language.EN).build();
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

        Learner enLearner = Learner.builder().id(300L).user(user).language(Language.EN).build();
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
