package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * A spelling an entry answers for besides its headword: another spelling of one of its lexemes (colour under color),
 * an older or a mistaken one (daß, nao), an inflected form a writer was asked for and refused (signed under sign).
 * {@code normalizedAlias} is what a reader's word is looked up by. The rows of a headword are replaced whenever its
 * entry is published.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "entry_alias")
public class EntryAlias {
    @Id
    @UuidV7
    UUID id;

    String language;
    String headword;
    String alias;
    String normalizedAlias;
}
