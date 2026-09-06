package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.enums.BookImportMetadataStatus;
import com.almonium.learning.book.model.enums.BookImportStatus;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserBookImport {
    @Id
    UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(length = 500)
    String title;

    @Column(length = 300)
    String author;

    @Column(columnDefinition = "text")
    String description;

    @Enumerated(EnumType.STRING)
    Language language;

    Integer publicationYear;

    @Enumerated(EnumType.STRING)
    BookImportStatus status;

    @Enumerated(EnumType.STRING)
    BookImportMetadataStatus metadataStatus;

    /** Who supplied each bibliographic field: "user", "source" (the file header), or "ai". */
    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, String> metadataProvenance;

    int progress;
    int wordCount;

    @Column(columnDefinition = "text")
    String error;

    @CreatedDate
    Instant createdAt;

    @LastModifiedDate
    Instant updatedAt;
}
