package com.almonium.learning.lexicon.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
 * A sentence showing the sense. {@code origin} says whether the writer made it up ("generated") or it was taken
 * from a book ("occurrence"); {@code translation} maps a language to its rendering when one exists.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
@Table(schema = "lexicon", name = "sense_example")
public class SenseExample {
    public static final String GENERATED = "generated";

    @Id
    @UuidV7
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sense_id")
    Sense sense;

    int position;
    String text;
    String origin;

    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, String> translation;
}
