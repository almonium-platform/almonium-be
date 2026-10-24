package com.almonium.learning.wanted.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.catalog.model.entity.CatalogWork;
import com.almonium.learning.wanted.model.enums.WantStatus;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.uuid.UuidV7;
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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * One member wanting one catalogue work in one language (W1). A want for a missing book and a want for a missing
 * language of a book already on the shelf are the same record: the catalogue work says which. Wanting is free and
 * promises nothing but a place in a public order; editorial still chooses what to prepare.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "want")
public class Want {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "catalog_work_id", nullable = false)
    CatalogWork catalogWork;

    @Enumerated(EnumType.STRING)
    Language language;

    @Enumerated(EnumType.STRING)
    WantStatus status;

    @ManyToOne
    @JoinColumn(name = "decided_by_user_id")
    User decidedBy;

    Instant decidedAt;

    // The edition that settled the want outlives its own withdrawal, which hides it from ordinary Book queries.
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "arrived_book_id")
    Book arrivedBook;

    Instant arrivedAt;

    /**
     * The library's text the reader asked for, by the address the processor fetches it from, when they overrode the
     * default (W1); null to take the catalogue's best. The first reservation's choice wins for everyone waiting.
     */
    String sourceDownloadUrl;

    @CreatedDate
    Instant createdAt;
}
