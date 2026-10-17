package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.lexicon.model.enums.LexiconStatus;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
 * One word of one language in one part of speech: the unit a dictionary entry is made of. {@code headword} is the
 * spelling the entry was written under; {@code lemma} and {@code pos} with {@code homographNo} identify the lexeme,
 * the number telling apart two lexemes of one lemma and pos (two unrelated nouns spelled alike). Everything hanging
 * off a lexeme is replaced wholesale when its entry is published again, except pronunciations, which are updated in
 * place so audio clips keep pointing at them.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "lexeme")
public class Lexeme {
    @Id
    @UuidV7
    UUID id;

    String language;
    String headword;
    String lemma;
    String pos;
    int homographNo;
    String kind;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> grammar = new LinkedHashMap<>();

    String pattern;
    BigDecimal zipf;
    int entryVersion;
    LexiconStatus status;
    Instant createdAt;
    Instant updatedAt;

    @OneToMany(mappedBy = "lexeme", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    @BatchSize(size = 50)
    List<LexemeForm> forms = new ArrayList<>();

    @OneToMany(mappedBy = "lexeme", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("variety, rank")
    @BatchSize(size = 50)
    List<Pronunciation> pronunciations = new ArrayList<>();

    @OneToMany(mappedBy = "lexeme", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    @BatchSize(size = 50)
    List<Sense> senses = new ArrayList<>();

    @OneToMany(mappedBy = "lexeme", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    @BatchSize(size = 50)
    List<LexemeRelation> relations = new ArrayList<>();

    @OneToMany(mappedBy = "lexeme", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position")
    @BatchSize(size = 50)
    List<LexemePart> parts = new ArrayList<>();

    @OneToMany(mappedBy = "lexeme", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("builtAt DESC")
    @BatchSize(size = 50)
    List<LexiconBuild> builds = new ArrayList<>();

    public LexemeForm addForm(LexemeForm form) {
        form.setLexeme(this);
        form.setLanguage(language);
        form.setPosition(forms.size() + 1);
        forms.add(form);
        return form;
    }

    public Pronunciation addPronunciation(Pronunciation pronunciation) {
        pronunciation.setLexeme(this);
        pronunciations.add(pronunciation);
        return pronunciation;
    }

    public Sense addSense(Sense sense) {
        sense.setLexeme(this);
        sense.setPosition(senses.size() + 1);
        senses.add(sense);
        return sense;
    }

    public LexemeRelation addRelation(LexemeRelation relation) {
        relation.setLexeme(this);
        relation.setPosition(relations.size() + 1);
        relations.add(relation);
        return relation;
    }

    public LexiconBuild addBuild(LexiconBuild build) {
        build.setLexeme(this);
        builds.add(0, build);
        return build;
    }

    /** The most recent build, which is where provenance comes from; null only for a row written outside publishing. */
    public LexiconBuild latestBuild() {
        return builds.isEmpty() ? null : builds.getFirst();
    }
}
