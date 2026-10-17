package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.lexicon.model.enums.LexiconStatus;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * How one variety (en-US, de-DE) says a lexeme, or one of its forms when {@code form} is set. Audio clips are keyed
 * by this row, so publishing an entry again updates it in place instead of replacing it. {@code reading} and
 * {@code engineInput} are filled by the audio phase: a spelling with stress marks or the exact text a speech engine
 * was given.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "pronunciation")
public class Pronunciation {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lexeme_id")
    Lexeme lexeme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lexeme_form_id")
    LexemeForm form;

    String variety;
    String phonemic;
    String reading;
    String engineInput;
    LexiconStatus status;
    String source;
}
