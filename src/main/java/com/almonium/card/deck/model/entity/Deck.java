package com.almonium.card.deck.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

/**
 * An ordered, titled list of a learner's words. The owner's title and order are part of what they share, so a deck of
 * one is still a deck and never collapses into a lone card.
 *
 * <p>A deck is deleted softly: a link to a deleted deck must say so rather than fall through to a generic not-found,
 * because a visitor who added words from it earlier wants to know whether they lost them.
 */
@Entity
@Table(name = "deck")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Deck {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    Learner owner;

    @Enumerated(EnumType.STRING)
    Language language;

    String title;

    /** The short id the public link carries. Fixed for the life of the deck; the switch turns it on and off. */
    @Column(name = "share_id")
    String shareId;

    @Builder.Default
    boolean shareEnabled = false;

    @CreatedDate
    Instant createdAt;

    @LastModifiedDate
    Instant updatedAt;

    Instant deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "deck")
    @OrderBy("position ASC")
    List<DeckItem> items = new ArrayList<>();

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
