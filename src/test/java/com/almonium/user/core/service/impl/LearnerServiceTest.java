package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.service.CardService;
import com.almonium.infra.chat.service.StreamChatService;
import com.almonium.subscription.model.entity.enums.PlanFeature;
import com.almonium.subscription.service.PlanValidationService;
import com.almonium.user.core.dto.TargetLanguageWithProficiency;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.mapper.LearnerMapper;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.LearnerService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    LearnerMapper learnerMapper;

    @Mock
    PlanValidationService planValidationService;

    @Mock
    CardService cardService;

    @Mock
    UserRepository userRepository;

    @Mock
    StreamChatService streamChatService;

    @InjectMocks
    LearnerService learnerService;

    @DisplayName("Should add multiple target languages when replacing existing ones")
    @Test
    void givenUserAndMultipleLanguages_whenCreateLearnersWithReplace_thenLearnersAreDeletedAndCreated() {
        // Arrange
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .learners(List.of(Learner.builder().id(id).language(Language.EN).build()))
                .build();

        List<TargetLanguageWithProficiency> languages = List.of(
                new TargetLanguageWithProficiency(Language.FR, CEFR.A1),
                new TargetLanguageWithProficiency(Language.DE, CEFR.B1));

        when(learnerRepository.countLearnersByUserId(user.getId())).thenReturn(0);
        when(userRepository.findUserWithLearners(user.getId())).thenReturn(Optional.of(user));

        // Act
        learnerService.createLearners(languages, user, true);

        // Assert
        // Ensure all previous learners are deleted
        verify(learnerRepository).deleteAllByUserId(user.getId());

        // Validate plan feature for the new count of target languages
        verify(planValidationService).validatePlanFeature(user, PlanFeature.MAX_TARGET_LANGS, 2);

        // Ensure new learners are saved with correct languages and CEFR levels
        verify(learnerRepository)
                .save(argThat(learner -> learner.getLanguage() == Language.FR
                        && learner.getSelfReportedLevel() == CEFR.A1
                        && learner.getUser().equals(user)));
        verify(learnerRepository)
                .save(argThat(learner -> learner.getLanguage() == Language.DE
                        && learner.getSelfReportedLevel() == CEFR.B1
                        && learner.getUser().equals(user)));

        // Optionally, verify the mapper is called if needed for toDto
        verify(learnerMapper).toDto(anyList());
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
        when(learnerRepository.findByUserIdAndLanguage(user.getId(), Language.FR))
                .thenReturn(Optional.empty());
        when(learnerRepository.findByUserIdAndLanguage(user.getId(), Language.DE))
                .thenReturn(Optional.empty());
        when(userRepository.findUserWithLearners(user.getId())).thenReturn(Optional.of(user));

        // Act
        learnerService.createLearners(languages, user, false);

        // Assert
        // Ensure existing learners are not deleted
        verify(learnerRepository, never()).deleteAllByUserId(user.getId());

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

    @DisplayName("Should throw exception if a target language already exists")
    @Test
    void givenExistingTargetLanguage_whenCreateLearners_thenThrowsException() {
        // Arrange
        User user = User.builder().id(UUID.randomUUID()).build();
        List<TargetLanguageWithProficiency> languages =
                List.of(new TargetLanguageWithProficiency(Language.FR, CEFR.A1));

        when(learnerRepository.findByUserIdAndLanguage(user.getId(), Language.FR))
                .thenReturn(Optional.of(
                        Learner.builder().user(user).language(Language.FR).build()));

        // Act & Assert
        assertThatThrownBy(() -> learnerService.createLearners(languages, user, false))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("You already have this target language");
        verify(learnerRepository, never()).save(any(Learner.class));
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
