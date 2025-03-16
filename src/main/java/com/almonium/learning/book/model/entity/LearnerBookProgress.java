package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.Learner;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
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

    Instant startedAt;
    Instant lastReadAt;
}
