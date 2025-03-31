package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.Learner;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "book_favorite",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"book_id", "learner_id"})})
@EqualsAndHashCode(of = {"id"})
public class BookFavorite {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    Book book;

    @ManyToOne
    @JoinColumn(name = "learner_id", referencedColumnName = "id")
    Learner learner;

    public BookFavorite(Learner learner, Book book) {
        this.learner = learner;
        this.book = book;
    }
}
