package com.linguatool.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    uniqueConstraints=
    @UniqueConstraint(columnNames={"requester_id", "requestee_id"})
)
@NoArgsConstructor
@AllArgsConstructor
//@IdClass(FriendshipPK.class)
public class Friendship implements Serializable {

//    @Id
//    @Column(name = "requester_id")
//    private Long requesterId;
//
//    @Id
//    @Column(name = "requestee_id")
//    private Long requesteeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
//    @ToString.Exclude
    @JoinColumn(name = "requester_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User requester;

//    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "requestee_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User requestee;

    @Column(name = "requestee_id")
    private Long requesteeId;

    @Column(name = "requester_id")
    private Long requesterId;

    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updated;

    @Column(name = "status")
    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Friendship that = (Friendship) o;
        return Objects.equals(id, that.id) && Objects.equals(requester, that.requester) && Objects.equals(requestee, that.requestee) && Objects.equals(created, that.created) && Objects.equals(updated, that.updated) && status == that.status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
            "id=" + id +
            '}';

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requester, requestee, created, updated, status);
    }
}

