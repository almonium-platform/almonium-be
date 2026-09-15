package com.almonium.card.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** A set-aside language is read-only, and that has to be true of the API and not only of the button. */
@ExtendWith(MockitoExtension.class)
class LearnerFinderTest {

    private static final UUID USER_ID = UUID.randomUUID();

    @Mock
    LearnerRepository learnerRepository;

    @InjectMocks
    LearnerFinder learnerFinder;

    private final User user = User.builder().id(USER_ID).build();

    @DisplayName("Should hand back a language that is active")
    @Test
    void givenActiveLearner_whenFindingOneToWriteTo_thenItIsReturned() {
        Learner learner = Learner.builder().language(Language.DE).active(true).build();
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.DE)).thenReturn(Optional.of(learner));

        assertThat(learnerFinder.findActiveLearner(user, Language.DE)).isSameAs(learner);
    }

    @DisplayName("Should refuse to hand back a language that has been set aside")
    @Test
    void givenSetAsideLearner_whenFindingOneToWriteTo_thenItIsRefused() {
        Learner learner = Learner.builder().language(Language.DE).active(false).build();
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.DE)).thenReturn(Optional.of(learner));

        assertThatThrownBy(() -> learnerFinder.findActiveLearner(user, Language.DE))
                .isInstanceOf(BadUserRequestActionException.class)
                .hasMessageContaining("set aside");
    }

    @DisplayName("Should still report a language the user does not have as missing, not as set aside")
    @Test
    void givenNoLearner_whenFindingOneToWriteTo_thenItIsReportedMissing() {
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.IT)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> learnerFinder.findActiveLearner(user, Language.IT))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Should leave reading a set-aside language alone")
    @Test
    void givenSetAsideLearner_whenFindingItToRead_thenItIsReturned() {
        Learner learner = Learner.builder().language(Language.DE).active(false).build();
        when(learnerRepository.findByUserIdAndLanguage(USER_ID, Language.DE)).thenReturn(Optional.of(learner));

        assertThatCode(() -> learnerFinder.findLearner(user, Language.DE)).doesNotThrowAnyException();
    }
}
