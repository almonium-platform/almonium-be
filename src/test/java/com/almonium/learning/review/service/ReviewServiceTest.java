package com.almonium.learning.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.learning.review.dto.ReviewAnswerRequest;
import com.almonium.learning.review.dto.ReviewAnswerResponse;
import com.almonium.learning.review.model.ConfusionEdge;
import com.almonium.learning.review.model.ReviewEvent;
import com.almonium.learning.review.model.ReviewOutcome;
import com.almonium.learning.review.model.ReviewPrompt;
import com.almonium.learning.review.model.ReviewPromptType;
import com.almonium.learning.review.model.ReviewSession;
import com.almonium.learning.review.model.ReviewSessionItem;
import com.almonium.learning.review.repository.ConfusionEdgeRepository;
import com.almonium.learning.review.repository.ReviewEventRepository;
import com.almonium.learning.review.repository.ReviewPromptRepository;
import com.almonium.learning.review.repository.ReviewSessionItemRepository;
import com.almonium.learning.review.repository.ReviewSessionRepository;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    LearnerFinder learnerFinder;

    @Mock
    LearningItemRepository learningItemRepository;

    @Mock
    ReviewPromptRepository promptRepository;

    @Mock
    ReviewEventRepository eventRepository;

    @Mock
    ConfusionEdgeRepository confusionEdgeRepository;

    @Mock
    ReviewSessionRepository sessionRepository;

    @Mock
    ReviewSessionItemRepository sessionItemRepository;

    @InjectMocks
    ReviewService reviewService;

    private User user;
    private Learner learner;
    private ReviewSession session;
    private LearningItem item;
    private ReviewPrompt prompt;

    @BeforeEach
    void setUp() {
        user = User.builder().id(UUID.randomUUID()).build();
        learner = Learner.builder()
                .id(UUID.randomUUID())
                .user(user)
                .language(Language.DE)
                .build();
        session = ReviewSession.builder()
                .id(UUID.randomUUID())
                .owner(user)
                .language(Language.DE)
                .createdAt(Instant.now())
                .build();
        item = LearningItem.builder()
                .id(UUID.randomUUID())
                .owner(learner)
                .language(Language.DE)
                .entry("die Ausgabe")
                .normalizedForm("die ausgabe")
                .dueAt(Instant.now().minusSeconds(1))
                .translations(new ArrayList<>(
                        List.of(Translation.builder().translation("edition").build())))
                .learningIntents(new HashSet<>(List.of(LearningIntent.PRODUCE)))
                .build();
        prompt = ReviewPrompt.builder()
                .id(UUID.randomUUID())
                .learningItem(item)
                .intent(LearningIntent.PRODUCE)
                .promptType(ReviewPromptType.FORM_RECALL)
                .promptText("edition")
                .expectedAnswer("die Ausgabe")
                .build();
    }

    @Test
    void correctAnswerAfterHintIsRecordedAsRecoveredAndScheduledByFsrs() {
        arrangeAnswer();
        ReviewAnswerResponse response = reviewService.answer(
                user,
                session.getId(),
                item.getId(),
                new ReviewAnswerRequest(prompt.getId(), "die Ausgabe", List.of("GENDER"), false));

        assertThat(response.outcome()).isEqualTo(ReviewOutcome.RECOVERED_WITH_HINT);
        assertThat(item.getFsrsCardJson()).isNotBlank();
        assertThat(item.getDueAt()).isAfter(item.getLastReviewedAt());
        ArgumentCaptor<ReviewEvent> event = ArgumentCaptor.forClass(ReviewEvent.class);
        verify(eventRepository).save(event.capture());
        assertThat(event.getValue().getHintsOpened()).isEqualTo("GENDER");
        assertThat(event.getValue().getFsrsCardAfter()).isNotBlank();
    }

    @Test
    void answerMatchingAnotherOwnedItemCreatesDirectionalConfusion() {
        arrangeAnswer();
        LearningItem confused = LearningItem.builder()
                .id(UUID.randomUUID())
                .owner(learner)
                .entry("die Aufgabe")
                .normalizedForm("die aufgabe")
                .translations(new ArrayList<>(
                        List.of(Translation.builder().translation("task").build())))
                .build();
        when(learningItemRepository.findAllByOwner(learner)).thenReturn(List.of(item, confused));
        when(confusionEdgeRepository.findByOwnerAndSourceItemAndTargetItem(user, item, confused))
                .thenReturn(Optional.empty());
        when(confusionEdgeRepository.save(any(ConfusionEdge.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReviewAnswerResponse response = reviewService.answer(
                user,
                session.getId(),
                item.getId(),
                new ReviewAnswerRequest(prompt.getId(), "die Aufgabe", List.of(), false));

        assertThat(response.outcome()).isEqualTo(ReviewOutcome.CONFUSED);
        assertThat(response.confusedWith().itemId()).isEqualTo(confused.getId());
        assertThat(confused.getDueAt()).isEqualTo(item.getDueAt());
        assertThat(confused.getFsrsCardJson()).isNotBlank();
        ArgumentCaptor<ConfusionEdge> edge = ArgumentCaptor.forClass(ConfusionEdge.class);
        verify(confusionEdgeRepository).save(edge.capture());
        assertThat(edge.getValue().getSourceItem()).isSameAs(item);
        assertThat(edge.getValue().getTargetItem()).isSameAs(confused);
    }

    private void arrangeAnswer() {
        ReviewSessionItem sessionItem = ReviewSessionItem.builder()
                .id(UUID.randomUUID())
                .session(session)
                .learningItem(item)
                .position(0)
                .build();
        when(sessionRepository.findByIdAndOwnerId(session.getId(), user.getId()))
                .thenReturn(Optional.of(session));
        when(sessionItemRepository.findBySessionIdAndLearningItemId(session.getId(), item.getId()))
                .thenReturn(Optional.of(sessionItem));
        when(promptRepository.findById(prompt.getId())).thenReturn(Optional.of(prompt));
        when(eventRepository.save(any(ReviewEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(sessionItemRepository.countBySessionIdAndCompletedAtIsNotNull(session.getId()))
                .thenReturn(1L);
        when(sessionItemRepository.findAllBySessionIdOrderByPosition(session.getId()))
                .thenReturn(List.of(sessionItem));
    }
}
