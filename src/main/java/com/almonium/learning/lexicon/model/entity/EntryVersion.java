package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * One publish of one headword, kept whole and never changed: the entry document as it was published, with the key
 * of every sense written into it ({@code lexemes[].senses[].key}) and, under {@code retired}, the keys of senses
 * the publish dropped, each with the key of the sense it most resembles or null. Rows are not tied to the lexeme
 * tables, so a lexeme a later rewrite removes keeps its history. A card's sense key finds every version it is in.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "entry_version")
public class EntryVersion {
    @Id
    @UuidV7
    UUID id;

    String language;
    String headword;
    int version;
    String writer;
    String schemaVersion;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> document;

    Instant builtAt;
}
