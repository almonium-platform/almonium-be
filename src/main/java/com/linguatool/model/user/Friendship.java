package com.linguatool.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(
    uniqueConstraints =
    @UniqueConstraint(columnNames = {"requester_id", "requestee_id"})
)
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FriendshipPK.class)
public class Friendship implements Serializable {

    @Id
    @Column(name = "requester_id")
    private Long requesterId;

    @Id
    @Column(name = "requestee_id")
    private Long requesteeId;

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "requestee_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User requestee;


    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updated;

    @Column(name = "status")
    private FriendshipStatus friendshipStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Friendship that = (Friendship) o;
        return Objects.equals(requester, that.requester) && Objects.equals(requestee, that.requestee) && Objects.equals(requesteeId, that.requesteeId) && Objects.equals(requesterId, that.requesterId) && Objects.equals(created, that.created) && Objects.equals(updated, that.updated) && friendshipStatus == that.friendshipStatus;
    }

    @Override
    public String toString() {
        return "Friendship{" +
            "requesterId=" + requesterId +
            ", requesteeId=" + requesteeId +
            ", created=" + created +
            ", updated=" + updated +
            ", status=" + friendshipStatus +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, requestee, created, updated, friendshipStatus);
    }


    public Long whoDeniesFriendship() {
        if (this.getFriendshipStatus().equals(FriendshipStatus.FST_BLOCKED_SND)) {
            return requesterId;
        }
        if (this.getFriendshipStatus().equals(FriendshipStatus.SND_BLOCKED_FST)) {
            return requesteeId;
        }
        return null;
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(name = "requestee_id")
//    private Long requesteeId;
//    @Column(name = "requester_id")
//    private Long requesterId;

}

