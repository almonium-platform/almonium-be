package com.almonium.user.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.suggestion.model.entity.CardSuggestion;
import com.almonium.learning.book.model.entity.LearnerBookProgress;
import com.almonium.user.core.model.enums.SetAsideBy;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Learner {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @Enumerated(EnumType.STRING)
    Language language;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    CEFR selfReportedLevel;

    @Builder.Default
    @OneToMany(mappedBy = "owner")
    List<LearningItem> learningItems = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "learner")
    List<LearnerBookProgress> bookProgresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender")
    List<CardSuggestion> outgoingSuggestions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recipient")
    List<CardSuggestion> incomingSuggestions = new ArrayList<>();

    /** When this language was taken up, so the record can say how long the commitment has run. */
    @CreatedDate
    Instant createdAt;

    /**
     * When this language was set aside, so the weeks since can be frozen rather than counted as missed. Null while
     * the language is active.
     */
    Instant setAsideAt;

    /**
     * Days per week the learner asks of themselves in this language: {@code null} until they choose, {@code 0} for
     * the deliberate "no target", otherwise 1, 2 or 4.
     */
    Integer weeklyTarget;

    /** Who set it aside, so a re-upgrade returns what the downgrade took and nothing the user chose to put down. */
    @Enumerated(EnumType.STRING)
    SetAsideBy setAsideBy;

    @Builder.Default
    boolean active = true;

    public void addLearningItem(LearningItem learningItem) {
        if (learningItem != null) {
            this.learningItems.add(learningItem);
            learningItem.setOwner(this);
        }
    }

    public Learner(User user, Language language, CEFR selfReportedLevel) {
        this.user = user;
        this.language = language;
        this.selfReportedLevel = selfReportedLevel;
        this.active = true;
    }
}
