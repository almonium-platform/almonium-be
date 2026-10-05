package com.almonium.learning.rights.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.rights.model.enums.RightsEffect;
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
 * /ops's decision for one work in one country ({@code *} for everywhere), which wins over the copyright engine. A
 * takedown is a BLOCK; an ALLOW shows a work where the engine's cautious guess would hide it. One per work and
 * country; deleting it hands the country back to the engine.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "rights_override")
public class RightsOverride {
    @Id
    @UuidV7
    UUID id;

    String workSlug;
    String country;

    @Enumerated(EnumType.STRING)
    RightsEffect effect;

    String reason;

    @ManyToOne
    @JoinColumn(name = "report_id")
    RightsReport report;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    User createdBy;

    @CreatedDate
    Instant createdAt;
}
