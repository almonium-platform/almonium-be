package com.almonium.card.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
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
    @UuidV7
    UUID id;

    @ColumnTransformer(read = "LOWER(text)")
    String text;

    @Builder.Default
    @OneToMany(mappedBy = "tag")
    Set<CardTag> cardTags = new HashSet<>();

    public Tag(String proposedName) {
        text = normalizeText(proposedName);
    }

    public Tag(UUID tagId, String name) {
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
