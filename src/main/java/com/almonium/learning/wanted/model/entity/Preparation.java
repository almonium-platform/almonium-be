package com.almonium.learning.wanted.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
 * A wanted work in a language being prepared from a free text: which source the book processor was asked to fetch,
 * the source edition it made, and its last reported progress, which the Wanted page shows as stages.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "preparation")
public class Preparation {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "catalog_work_id", nullable = false)
    CatalogWork catalogWork;

    @Enumerated(EnumType.STRING)
    Language language;

    String sourceKind;
    String sourceReference;
    String sourceLanguage;
    UUID processorEditionId;

    /** The processor's phase: downloading, ingesting, review, ready, published or failed. */
    String phase;

    /** The processor's running pipeline stage, when it reports one. */
    String stage;

    /** How many of the four reader-facing stages are done (text, chapters, level and words, on the shelf). */
    Integer progress;

    String error;

    /** Nobody for a preparation the factory started. */
    @ManyToOne
    @JoinColumn(name = "started_by_user_id")
    User startedBy;

    /** Who reports progress: {@code processor}, polled from this deployment's processor, or {@code factory}, pushed. */
    String reporter;

    @CreatedDate
    Instant createdAt;

    Instant checkedAt;
}
