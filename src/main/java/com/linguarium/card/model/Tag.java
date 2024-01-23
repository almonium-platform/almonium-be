package com.linguarium.card.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {
    private static final String WHITESPACE_PATTERN = "\\s+";
    private static final String CONNECTING_SYMBOL = "_";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    Long id;

    @Column(name = "text")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @PrePersist
    @PreUpdate
    private void prepareData() {
        text = normalizeText(text);
    }
}
