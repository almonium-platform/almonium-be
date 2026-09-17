package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

@Entity
// A withdrawn book is invisible to every catalogue and reader query, including
// the language-variant subselects, but its row stays: learner progress,
// favourites and translation orders reference it and must survive a takedown.
@SQLRestriction("withdrawn_at is null")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class Book {
    @Id
    @UuidV7
    UUID id;

    String editionSlug;
    String workSlug;
    String sourceHash;

    // Reference to original book (null if this IS the original)
    @ManyToOne
    @JoinColumn(name = "original_book_id")
    Book originalBook;

    String title;
    String author;
    String description;
    int publicationYear;
    String coverUrl;
    int wordCount;

    /** How many chapters the processor published; what the shelf's "chapter 3 of 24" counts against. Null before it was sent. */
    Integer chapterCount;

    @Enumerated(EnumType.STRING)
    Language language;

    @Enumerated(EnumType.STRING)
    Language originalLanguage;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    CEFR cefrLevel;

    String editionType;
    String translator;

    /** Editorial's one sentence about this edition, shown under the Edition chips on the book page (G15). */
    @Column(length = 500)
    String editionNote;

    Instant withdrawnAt;
}
