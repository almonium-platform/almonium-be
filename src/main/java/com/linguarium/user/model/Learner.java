package com.linguarium.user.model;
import javax.persistence.*;


import com.linguarium.card.model.Card;
import com.linguarium.translator.model.LanguageEntity;
import com.linguarium.suggestion.model.CardSuggestion;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.List;
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
    private User user;

    @OneToMany(mappedBy = "owner")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Card> cards;

    @OneToMany(mappedBy = "sender")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<CardSuggestion> outgoingSuggestions;

    @OneToMany(mappedBy = "recipient")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<CardSuggestion> incomingSuggestions;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "user_target_lang",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lang_id", referencedColumnName = "id")}
    )
    Set<LanguageEntity> targetLangs;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "user_fluent_lang",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "lang_id", referencedColumnName = "id")}
    )
    Set<LanguageEntity> fluentLangs;

    public void addCard(Card card) {
        if (card != null) {
            this.cards.add(card);
            card.setOwner(this);
        }
    }
}
