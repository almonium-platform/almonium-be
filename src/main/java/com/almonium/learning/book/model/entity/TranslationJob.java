package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.TranslationJobPhase;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * One approved translation of one book into one language: the processor job an operator started, and what we last
 * heard about it. The estimate is written the moment the job is approved; that is the cost ledger the budget reads.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "translation_job")
public class TranslationJob {
    @Id
    @UuidV7
    UUID id;

    /** Always the original edition; a translation is never translated. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    Book book;

    @Enumerated(EnumType.STRING)
    Language language;

    UUID processorEditionId;
    String processorEditionSlug;

    @Enumerated(EnumType.STRING)
    TranslationJobPhase phase;

    Integer progressCompleted;
    Integer progressTotal;

    @Column(precision = 12, scale = 6)
    BigDecimal estimatedCostUsd;

    @Column(precision = 12, scale = 6)
    BigDecimal actualCostUsd;

    String tier;
    String mode;

    @ManyToOne
    @JoinColumn(name = "approved_by_user_id")
    User approvedBy;

    Instant approvedAt;
    Instant finishedAt;

    @Column(columnDefinition = "text")
    String error;

    @ManyToOne
    @JoinColumn(name = "published_book_id")
    Book publishedBook;

    Instant lastSyncedAt;

    @CreatedDate
    Instant createdAt;
}
