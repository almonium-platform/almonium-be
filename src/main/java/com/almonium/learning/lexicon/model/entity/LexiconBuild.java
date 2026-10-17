package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
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
 * One publish of one lexeme: which writer made it, with which model, prompt and register vocabulary, what it cost
 * and how many tokens it took. Rows accumulate; the newest is the lexeme's provenance. {@code meta} keeps the
 * writer's whole metadata record, {@code verdicts} the judges' when a review pass has run.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "lexicon_build")
public class LexiconBuild {
    @Id
    @UuidV7
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lexeme_id")
    Lexeme lexeme;

    String writer;
    String model;
    String promptVersion;
    String registerVersion;
    String schemaVersion;
    String effort;
    BigDecimal listCostUsd;
    BigDecimal relabelCostUsd;
    Integer inputTokens;
    Integer outputTokens;
    Integer cacheReadTokens;
    Integer cacheWriteTokens;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> verdicts;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> meta;

    Instant builtAt;
}
