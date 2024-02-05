package com.linguarium.user.model;

import com.linguarium.card.model.Card;
import com.linguarium.suggestion.model.CardSuggestion;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Learner {
    @Id
    @Column(name = "id")
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    User user;

    @OneToMany(mappedBy = "owner")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Card> cards;

    @OneToMany(mappedBy = "sender")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<CardSuggestion> outgoingSuggestions;

    @OneToMany(mappedBy = "recipient")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<CardSuggestion> incomingSuggestions;

    @ElementCollection
    @CollectionTable(name = "learner_target_lang", joinColumns = @JoinColumn(name = "learner_id"))
    @Column(name = "lang")
    Set<String> targetLangs; // TODO why not enum

    @ElementCollection
    @CollectionTable(name = "learner_fluent_lang", joinColumns = @JoinColumn(name = "learner_id"))
    @Column(name = "lang")
    Set<String> fluentLangs;

    public void addCard(Card card) {
        if (card != null) {
            this.cards.add(card);
            card.setOwner(this);
        }
    }
}
