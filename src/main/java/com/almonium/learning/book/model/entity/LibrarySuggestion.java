package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.LibrarySuggestionStatus;
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
 * A private import its owner offered for the public library. The bibliographic fields are copied at the moment of
 * suggesting, so the reviewer sees what the owner vouched for even if the import is edited afterwards.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "library_suggestion")
public class LibrarySuggestion {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_import_id", nullable = false)
    UserBookImport bookImport;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(length = 500)
    String title;

    @Column(length = 300)
    String author;

    @Enumerated(EnumType.STRING)
    Language language;

    Integer publicationYear;

    @Column(columnDefinition = "text")
    String description;

    @Enumerated(EnumType.STRING)
    LibrarySuggestionStatus status;

    UUID processorEditionId;
    String processorEditionSlug;
    String phase;
    int progress;

    @Column(columnDefinition = "text")
    String error;

    @ManyToOne
    @JoinColumn(name = "library_book_id")
    Book libraryBook;

    @ManyToOne
    @JoinColumn(name = "decided_by_user_id")
    User decidedBy;

    Instant decidedAt;
    Instant publishedAt;

    @CreatedDate
    Instant createdAt;
}
