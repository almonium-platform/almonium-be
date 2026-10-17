package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

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
 * lexeme (a countable sense of a mass noun); {@code usage} is the short label a dictionary prints after the sense;
 * {@code regions} the places the sense is used, when the writer named them.
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

    int position;
    String definition;
    String cefr;
    String cefrSource;
    String register;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> grammar;

    String usage;

    @JdbcTypeCode(SqlTypes.JSON)
    List<String> regions;

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
