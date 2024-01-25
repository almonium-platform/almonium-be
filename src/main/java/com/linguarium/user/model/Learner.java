package com.linguarium.user.model;

import com.linguarium.card.model.Card;
import com.linguarium.suggestion.model.CardSuggestion;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    Set<String> targetLangs;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Learner learner = (Learner) o;

        return Objects.equals(id, learner.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
