package com.linguarium.friendship.model;

import com.linguarium.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"requester_id", "requestee_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requester_id")
    private Long requesterId;

    @Column(name = "requestee_id")
    private Long requesteeId;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "requestee_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User requestee;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime updated;

    @Column(name = "status")
    private FriendshipStatus status;

    public Friendship(Long requesterId, Long requesteeId) {
        this.requesterId = requesterId;
        this.requesteeId = requesteeId;
        status = FriendshipStatus.PENDING;
    }

    public Friendship(User requester, User requestee) {
        this.requester = requester;
        this.requestee = requestee;
        this.requesterId = requester.getId();
        this.requesteeId = requestee.getId();
        status = FriendshipStatus.PENDING;
    }

    public Optional<Long> getFriendshipDenier() {
        if (this.getStatus().equals(FriendshipStatus.FST_BLOCKED_SND)) {
            return requesterId.describeConstable();
        }
        if (this.getStatus().equals(FriendshipStatus.SND_BLOCKED_FST)) {
            return requesteeId.describeConstable();
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Friendship{"
                + "requesterId=" + requester.getId()
                + ", requesteeId=" + requestee.getId()
                + ", created=" + created
                + ", updated=" + updated
                + ", status=" + status
                + '}';
    }
}
