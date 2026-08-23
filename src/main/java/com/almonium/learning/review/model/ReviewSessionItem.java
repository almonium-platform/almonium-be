package com.almonium.learning.review.model;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
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
public class ReviewSessionItem {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    ReviewSession session;

    @ManyToOne
    @JoinColumn(name = "learning_item_id", nullable = false)
    LearningItem learningItem;

    int position;

    Instant completedAt;

    UUID reviewEventId;
}
