package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.Learner;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "learner_book_progress",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "learner_id"})})
public class LearnerBookProgress {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    Book book;

    @ManyToOne
    @JoinColumn(name = "learner_id", referencedColumnName = "id")
    Learner learner;

    int progressPercentage;

    @CreatedDate
    Instant startedAt;

    Instant lastReadAt;

    public LearnerBookProgress(Learner learner, Book book, int progressPercentage) {
        this.learner = learner;
        this.book = book;
        this.progressPercentage = progressPercentage;
    }
}
