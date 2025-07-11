package com.almonium.card.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Card {

    @Id
    @UuidV7
    UUID id;

    @Builder.Default
    UUID publicId = UUID.randomUUID();

    String entry;

    @CreatedDate
    Instant createdAt;

    @LastModifiedDate
    Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    Learner owner;

    @Enumerated(EnumType.STRING)
    Language language;

    @Builder.Default
    @OneToMany(mappedBy = "card")
    List<Example> examples = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "card")
    List<Translation> translations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "card")
    Set<CardTag> cardTags = new HashSet<>();

    @Builder.Default
    int iteration = 0;

    int frequency;

    public void removeCardTag(CardTag cardTag) {
        if (cardTag != null) {
            cardTags.remove(cardTag);
        }
    }
}
