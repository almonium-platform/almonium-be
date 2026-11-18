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
 * by this row, so publishing an entry again updates it in place instead of replacing it. {@code engineInput} is
 * filled by the audio phase: the exact text a speech engine was given. {@code note} says why a variety has this
 * reading besides its first (older, casual, disputed), when it says something.
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

    /** 1-based, within the variety: a word one variety says two ways has rank 1 and rank 2. */
    int rank;

    /** The IPA transcription; null when the entry gives a reading only. */
    String phonemic;

    String note;

    /** The word as read, in {@code notation}: kana, pinyin, the spelling with its stress marked (рука́). */
    String reading;

    String notation;

    /** Japanese: the mora after which the pitch drops, 0 when it does not; null when not known. */
    Integer accent;

    /** The spelling this reading belongs to, when the lemma's spellings are said differently. */
    String spelling;

    String engineInput;
    LexiconStatus status;
    String source;
}
