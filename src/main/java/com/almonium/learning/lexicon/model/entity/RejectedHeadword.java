package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * A headword the writer refused to make an entry for, with its reason: an abbreviation, a foreign word, an inflected
 * form of another lemma, a misspelling. Kept so the headword lists know it was looked at; publishing lexemes for the
 * headword later removes the row.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "rejected_headword")
public class RejectedHeadword {
    @Id
    @UuidV7
    UUID id;

    String language;
    String headword;
    String reason;
    String writer;
    String model;
    String promptVersion;
    Instant rejectedAt;
}
