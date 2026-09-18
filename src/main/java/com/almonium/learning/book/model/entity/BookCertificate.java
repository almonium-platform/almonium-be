package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A book read to the end (design K): the one artifact a reader would post unprompted. Issued once, the moment the
 * last chapter is finished, and frozen then: the twelve rarest words and the counts are what they were on that day,
 * so the page and the link preview never drift. The reader decides on the spot whether the page is public.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(
        name = "book_certificate",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "book_id"})})
public class BookCertificate {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    Book book;

    Instant finishedAt;

    /** Whether {@code /read/@username/editionSlug} answers; off, the URL is a 404 and nothing about the reader shows. */
    boolean publicPage;

    /** The twelve rarest words the reader met, as they were met, rarest first. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> words;

    int wordsRead;

    /** Words saved from this edition when the certificate was last looked at by its owner. */
    int wordsSaved;

    public BookCertificate(User user, Book book, Instant finishedAt, boolean publicPage, List<String> words) {
        this.user = user;
        this.book = book;
        this.finishedAt = finishedAt;
        this.publicPage = publicPage;
        this.words = words;
        this.wordsRead = book.getWordCount();
    }
}
