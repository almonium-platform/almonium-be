package com.almonium.learning.review.model;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReviewPrompt {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "learning_item_id", nullable = false)
    LearningItem learningItem;

    @Enumerated(EnumType.STRING)
    LearningIntent intent;

    @Enumerated(EnumType.STRING)
    ReviewPromptType promptType;

    @Column(columnDefinition = "text")
    String promptText;

    @Column(columnDefinition = "text")
    String expectedAnswer;

    int position;

    @Builder.Default
    boolean active = true;
}
