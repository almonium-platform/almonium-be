package com.almonium.learning.rhythm.model;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * One calendar day, in the learner's own time zone, on which any learning happened.
 *
 * <p>A row exists only for days with activity. {@code met} is set by any completed learning event; the accumulated
 * seconds are texture for the rhythm band and never a threshold for whether the day counts.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
@Table(
        name = "learning_day",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "activity_date"})})
public class LearningDay {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "activity_date", nullable = false)
    LocalDate day;

    @Column(name = "seconds_learned", nullable = false)
    int secondsLearned;

    @Column(nullable = false)
    boolean met;
}
