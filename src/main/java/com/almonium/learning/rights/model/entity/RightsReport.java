package com.almonium.learning.rights.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.rights.model.enums.ReporterRole;
import com.almonium.learning.rights.model.enums.RightsReportStatus;
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
 * Someone saying a book should not be here, or not in their country: a rights holder, a reader, anyone. Guests may
 * report (rights holders are rarely members), so the reporter is either a member or an email address. The country
 * the request came from is kept beside the one the reporter names.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "rights_report")
public class RightsReport {
    @Id
    @UuidV7
    UUID id;

    // A report outlives the book's withdrawal, which hides the row from ordinary Book queries.
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "book_id")
    Book book;

    String workSlug;

    @ManyToOne
    @JoinColumn(name = "reporter_user_id")
    User reporter;

    String reporterEmail;
    String reporterName;

    @Enumerated(EnumType.STRING)
    ReporterRole reporterRole;

    /** The country the report is about, as the reporter names it; null for everywhere. */
    String country;

    /** The country the report was sent from, as the request's address places it. */
    String detectedCountry;

    String message;

    @Enumerated(EnumType.STRING)
    RightsReportStatus status;

    @ManyToOne
    @JoinColumn(name = "handled_by_user_id")
    User handledBy;

    Instant handledAt;

    @CreatedDate
    Instant createdAt;
}
