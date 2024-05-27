package linguarium.user.core.model;

import static lombok.AccessLevel.PRIVATE;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import java.util.Set;
import linguarium.card.core.model.Card;
import linguarium.card.suggestion.model.CardSuggestion;
import linguarium.engine.translator.model.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
public class Learner {
    @Id
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    User user;

    @OneToMany(mappedBy = "owner")
    Set<Card> cards;

    @OneToMany(mappedBy = "sender")
    List<CardSuggestion> outgoingSuggestions;

    @OneToMany(mappedBy = "recipient")
    List<CardSuggestion> incomingSuggestions;

    @ElementCollection(targetClass = Language.class)
    @CollectionTable(name = "learner_target_lang", joinColumns = @JoinColumn(name = "learner_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    Set<Language> targetLangs;

    @ElementCollection(targetClass = Language.class)
    @CollectionTable(name = "learner_fluent_lang", joinColumns = @JoinColumn(name = "learner_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "lang")
    Set<Language> fluentLangs;

    public void addCard(Card card) {
        if (card != null) {
            this.cards.add(card);
            card.setOwner(this);
        }
    }
}
