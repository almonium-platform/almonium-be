package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.BookRequestStatus;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * One member asking for one work in one language (G19). Rows of the same work and language are one row in /ops and
 * are decided together; the count of distinct members is the row's weight. {@code workSlug} is set when the work
 * already exists in another language, which is what makes the row an "Add edition" rather than a "New work". The
 * public-domain lookup result is stored at ask time so the reviewer sees what the reader was told.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "book_request")
public class BookRequest {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(length = 500, nullable = false)
    String title;

    @Column(length = 300, nullable = false)
    String author;

    /** Lower-cased, whitespace-folded "title|author": what groups asks and what the unique constraint is on. */
    @Column(length = 820, nullable = false)
    String normalizedKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Language language;

    @Column(length = 180)
    String workSlug;

    Integer gutenbergId;
    Integer publicationYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BookRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "decided_by_user_id")
    User decidedBy;

    Instant decidedAt;

    @ManyToOne
    @JoinColumn(name = "published_book_id")
    Book publishedBook;

    Instant publishedAt;

    @CreatedDate
    Instant createdAt;
}
