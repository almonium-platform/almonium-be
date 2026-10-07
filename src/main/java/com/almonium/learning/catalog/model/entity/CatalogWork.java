package com.almonium.learning.catalog.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.catalog.model.CatalogAuthor;
import com.almonium.learning.rights.model.RightsFacts;
import com.almonium.learning.rights.model.RightsStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
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
 * One notable book, on the shelf or not: what search lands on and what the copyright engine judges. Rows are written
 * in bulk by the catalogue import and keep their id across imports, so anything pointing at a work survives a
 * refresh. {@code workSlug} links the work to the shelf when the library has an edition of it.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(name = "catalog_work")
public class CatalogWork {
    @Id
    UUID id;

    String wikidataId;
    Integer gutenbergId;
    String title;
    String originalTitle;
    String originalLanguage;
    Integer publicationYear;
    String authorDisplay;

    @JdbcTypeCode(SqlTypes.JSON)
    List<CatalogAuthor> authors = new ArrayList<>();

    int popularity;

    @Column(length = 180)
    String workSlug;

    /** The engine's answer for the countries editions are made and hosted in, as of the last refresh. */
    @Enumerated(EnumType.STRING)
    RightsStatus productionStatus;

    Integer productionFreeFrom;

    /** Wikidata's publisher: research for a licensing conversation, not a rights verdict. */
    String publisher;

    Instant updatedAt;

    public RightsFacts rightsFacts() {
        return new RightsFacts(
                publicationYear, authors.stream().map(CatalogAuthor::facts).toList());
    }
}
