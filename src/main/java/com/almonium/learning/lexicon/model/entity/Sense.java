package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.lexicon.model.RegionScope;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * One meaning of a lexeme, in the order the entry lists them. {@code grammar} holds only what differs from the
 * lexeme (a countable sense of a mass noun); {@code regions} where it is used when not everywhere (British boot of a
 * car), null for the whole language. Glosses and cues are not in the entry: passes of their own write them.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "sense")
public class Sense {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lexeme_id")
    Lexeme lexeme;

    /**
     * Who this sense is across rewrites: the id the ingest step gave it, which it keeps while its definition stays
     * mostly the same. A republished entry recreates its rows; cards and entry versions name a sense by this key.
     */
    UUID senseKey;

    int position;
    String definition;
    String cefr;
    String cefrSource;

    /** What kind of word this is here, each axis by itself: formality, attitude, time, style, normativity, domain. */
    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> labels;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> grammar;

    /** How the word is built into a sentence, every open place marked (decide on sth). */
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> frames;

    /** The frames a program can read, each as {frame, parts}: the headword, the fixed words and the open places. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<Map<String, Object>> patterns;

    /** The words this sense is commonly said with, the less guessable first. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> collocations;

    /** Words that mean the opposite of this sense, {lemma, pos}. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<Map<String, Object>> antonyms;

    /** The spellings that carry this sense, when not all of the lemma's do. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> spellings;

    /** The standard word for a nonstandard or proscribed one. */
    String standardAlternative;

    @JdbcTypeCode(SqlTypes.JSON)
    List<RegionScope> regions;

    /** The evidence senses the writer said this one covers ("1.2"), which is how a rewrite is matched to it. */
    @JdbcTypeCode(SqlTypes.JSON)
    List<String> cited;

    @OneToMany(mappedBy = "sense", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fluentLanguage, rank")
    @BatchSize(size = 50)
    List<SenseGloss> glosses = new ArrayList<>();

    @OneToMany(mappedBy = "sense", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    @BatchSize(size = 50)
    List<SenseExample> examples = new ArrayList<>();

    @OneToMany(mappedBy = "sense", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    @BatchSize(size = 50)
    List<SenseCue> cues = new ArrayList<>();

    public SenseGloss addGloss(SenseGloss gloss) {
        gloss.setSense(this);
        glosses.add(gloss);
        return gloss;
    }

    public SenseExample addExample(SenseExample example) {
        example.setSense(this);
        example.setPosition(examples.size() + 1);
        examples.add(example);
        return example;
    }

    public SenseCue addCue(SenseCue cue) {
        cue.setSense(this);
        cue.setPosition(cues.size() + 1);
        cues.add(cue);
        return cue;
    }
}
