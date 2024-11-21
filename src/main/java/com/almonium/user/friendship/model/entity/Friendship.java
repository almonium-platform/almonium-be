package com.almonium.user.friendship.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.User;
import com.almonium.user.friendship.model.enums.FriendshipStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.Optional;
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
@Table(name = "friendship", uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id", "requestee_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

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
    FriendshipStatus status;

    public Friendship(User requester, User requestee) {
        this.requester = requester;
        this.requestee = requestee;
        status = FriendshipStatus.PENDING;
    }

    public Optional<Long> getFriendshipDenier() {
        if (this.getStatus().equals(FriendshipStatus.FST_BLOCKED_SND)) {
            return requester.getId().describeConstable();
        }
        if (this.getStatus().equals(FriendshipStatus.SND_BLOCKED_FST)) {
            return requestee.getId().describeConstable();
        }
        return Optional.empty();
    }
}
