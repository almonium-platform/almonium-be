package com.linguarium.card.core.model;

import static lombok.AccessLevel.PRIVATE;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
public class Tag {
    private static final String WHITESPACE_PATTERN = "\\s+";
    private static final String CONNECTING_SYMBOL = "_";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ColumnTransformer(read = "LOWER(text)")
    String text;

    @OneToMany(mappedBy = "tag")
    Set<CardTag> cardTags;

    public Tag(String proposedName) {
        text = normalizeText(proposedName);
    }

    public Tag(Long tagId, String name) {
        text = normalizeText(name);
        id = tagId;
    }

    public static String normalizeText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null for normalization");
        }
        return text.trim().replaceAll(WHITESPACE_PATTERN, CONNECTING_SYMBOL).toLowerCase();
    }

    @PrePersist
    @PreUpdate
    private void prepareData() {
        text = normalizeText(text);
    }
}
