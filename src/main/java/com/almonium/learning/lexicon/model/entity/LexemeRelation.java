package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
 * A link from a lexeme to another lemma, by its type: what it derives from, its aspect partner, a look-alike. The
 * target is named by lemma and pos because the writer sees one headword at a time; {@code target} is resolved to a
 * lexeme when that lemma is in the lexicon, at publish time or when the target arrives later.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "lexeme_relation")
public class LexemeRelation {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lexeme_id")
    Lexeme lexeme;

    int position;
    String type;
    String targetLemma;
    String targetPos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_lexeme_id")
    Lexeme target;

    String evidence;

    /** The senses of this lexeme, counted from 1, that the relation holds for; null for all of them. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<Integer> senses;
}
