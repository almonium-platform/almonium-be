package com.almonium.learning.almo.model;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * One reply Almo generated, with what it cost. This is the row the cost ledger itemises and the row the daily
 * ceiling counts; it is keyed by the learner's message so a redelivered request cannot buy a second reply.
 */
@Entity
@Table(name = "almo_turn")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class AlmoTurn {
    @Id
    @UuidV7
    UUID id;

    UUID userId;

    @Enumerated(EnumType.STRING)
    Language language;

    String userMessageId;

    String replyMessageId;

    String model;

    int promptTokens;

    int completionTokens;

    Instant createdAt;
}
