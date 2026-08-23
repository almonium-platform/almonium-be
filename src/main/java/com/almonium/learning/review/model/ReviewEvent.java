package com.almonium.learning.review.model;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@IdClass(ReviewEventId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReviewEvent {
    @Id
    UUID id;

    @Id
    Instant reviewedAt;

    @ManyToOne
    @JoinColumn(name = "learning_item_id", nullable = false)
    LearningItem learningItem;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @ManyToOne
    @JoinColumn(name = "session_id")
    ReviewSession session;

    @Enumerated(EnumType.STRING)
    LearningIntent intent;

    @Enumerated(EnumType.STRING)
    ReviewPromptType promptType;

    @ManyToOne
    @JoinColumn(name = "prompt_id")
    ReviewPrompt prompt;

    @Column(columnDefinition = "text")
    String answer;

    @Column(columnDefinition = "text")
    String expectedAnswer;

    @Enumerated(EnumType.STRING)
    ReviewOutcome outcome;

    String hintsOpened;

    @ManyToOne
    @JoinColumn(name = "confused_with_item_id")
    LearningItem confusedWithItem;

    UUID correctsEventId;

    @Column(columnDefinition = "text")
    String fsrsCardBefore;

    @Column(columnDefinition = "text")
    String fsrsCardAfter;

    Instant dueBefore;

    Instant dueAfter;
}
