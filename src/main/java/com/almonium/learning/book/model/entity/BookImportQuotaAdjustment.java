package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.model.entity.enums.PlanFeature;
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** Support-issued quota credit/debit. Import history is never deleted to reset an allowance. */
@Getter
@Setter
@Entity
@Table(name = "book_import_quota_adjustment")
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = PRIVATE)
public class BookImportQuotaAdjustment {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "performed_by_user_id", nullable = false)
    User performedBy;

    @Enumerated(EnumType.STRING)
    PlanFeature featureKey;

    Instant periodStartsAt;
    int adjustment;
    String reason;

    @CreatedDate
    Instant createdAt;
}
