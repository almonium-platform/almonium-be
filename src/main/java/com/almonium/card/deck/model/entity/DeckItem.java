package com.almonium.card.deck.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** One word's place in a deck. The learning item stays the owner's; the deck only orders it. */
@Entity
@Table(name = "deck_item")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
public class DeckItem {

    @Id
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "deck_id", referencedColumnName = "id")
    Deck deck;

    @ManyToOne
    @JoinColumn(name = "learning_item_id", referencedColumnName = "id")
    LearningItem item;

    int position;
}
