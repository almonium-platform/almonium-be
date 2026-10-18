package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.lexicon.model.enums.ClipStatus;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * One rendering of a pronunciation by one speech engine: the engine, voice, model, pace and format identify it, and
 * {@code objectKey} says where its bytes are in the audio store. The bytes are written once and never replaced;
 * publishing the same rendering again updates this row and leaves the object alone. {@code transcript} is what the
 * verifier heard when it differed from the word, {@code characterCost} what the engine charged.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "audio_clip")
public class AudioClip {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pronunciation_id")
    Pronunciation pronunciation;

    String engine;
    String voice;
    String model;
    BigDecimal pace;
    String format;
    String objectKey;
    int byteSize;
    int durationMs;
    ClipStatus status;
    String transcript;
    BigDecimal characterCost;
    Instant createdAt;
}
