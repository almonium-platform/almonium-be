package com.almonium.card.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.card.core.model.enums.LearningItemType;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The durable thing a learner is acquiring. A review card is only one presentation of this item.
 *
 * <p>The physical table deliberately remains {@code card} during the expand/contract migration so
 * old application slots and mobile clients can continue to operate.
 */
@Entity
@Table(name = "card")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class LearningItem {

    @Id
    @UuidV7
    UUID id;

    @Builder.Default
    UUID publicId = UUID.randomUUID();

    String entry;

    String normalizedForm;

    String lemma;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    LearningItemType itemType = LearningItemType.WORD;

    String partOfSpeech;

    String selectedSense;

    String sourceContext;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "learning_item_intent", joinColumns = @JoinColumn(name = "learning_item_id"))
    @Column(name = "intent")
    @Enumerated(EnumType.STRING)
    Set<LearningIntent> learningIntents = new HashSet<>(Set.of(LearningIntent.UNDERSTAND));

    @CreatedDate
    Instant createdAt;

    @LastModifiedDate
    Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "learner_id", referencedColumnName = "id")
    Learner owner;

    @Column(name = "owner_id", nullable = false)
    UUID legacyOwnerId;

    @Enumerated(EnumType.STRING)
    Language language;

    @Builder.Default
    @OneToMany(mappedBy = "card")
    List<Example> examples = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "card")
    List<Translation> translations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "card")
    Set<CardTag> cardTags = new HashSet<>();

    @Builder.Default
    int iteration = 0;

    int frequency;

    @Column(columnDefinition = "text")
    String fsrsCardJson;

    Instant dueAt;

    Instant lastReviewedAt;

    @Builder.Default
    int totalReviews = 0;

    @Builder.Default
    int consecutiveFailures = 0;

    String failurePromptType;

    @Builder.Default
    boolean leech = false;

    public void removeCardTag(CardTag cardTag) {
        if (cardTag != null) {
            cardTags.remove(cardTag);
        }
    }
}
