package com.almonium.user.relationship.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.model.enums.RelationshipStatus;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.Optional;
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

@Entity
@Table(name = "relationship", uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id", "requestee_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
public class Relationship {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    User requester;

    @ManyToOne
    @JoinColumn(name = "requestee_id", referencedColumnName = "id")
    User requestee;

    @CreatedDate
    Instant createdAt;

    @LastModifiedDate
    Instant updatedAt;

    @Enumerated(EnumType.STRING)
    RelationshipStatus status;

    public Relationship(User requester, User requestee) {
        this.requester = requester;
        this.requestee = requestee;
        status = RelationshipStatus.PENDING;
    }

    public Optional<UUID> getRelationshipDenier() {
        if (this.getStatus().equals(RelationshipStatus.FST_BLOCKED_SND)) {
            return Optional.ofNullable(requester.getId());
        }
        if (this.getStatus().equals(RelationshipStatus.SND_BLOCKED_FST)) {
            return Optional.ofNullable(requestee.getId());
        }
        return Optional.empty();
    }
}
