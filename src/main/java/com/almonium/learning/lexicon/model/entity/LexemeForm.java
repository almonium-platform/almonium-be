package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.LinkedHashMap;
import java.util.List;
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
 * One inflected form of a lexeme with its Universal Dependencies features. {@code normalizedForm} (NFC, lowercase)
 * is what a reader's word is looked up by; {@code language} is copied from the lexeme so that lookup needs no join.
 * {@code reading} is how the form is read in {@code notation} (kana, the spelling with its stress marked), and
 * {@code note} where it is used when its features cannot say (only in the passive).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "lexeme_form")
public class LexemeForm {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lexeme_id")
    Lexeme lexeme;

    String language;
    int position;
    String form;
    String normalizedForm;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> features = new LinkedHashMap<>();

    /** The varieties that use the form (en-US for gotten), null for all of them. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> varieties;

    /** The keys of the senses the form belongs to, null for all of them. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<UUID> senseKeys;

    String reading;
    String notation;
    String note;
}
