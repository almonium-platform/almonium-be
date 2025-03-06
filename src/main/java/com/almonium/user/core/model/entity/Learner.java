package com.almonium.user.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.Card;
import com.almonium.card.suggestion.model.entity.CardSuggestion;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    @UuidV7
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @Enumerated(EnumType.STRING)
    Language language;

    @Enumerated(EnumType.STRING)
    CEFR selfReportedLevel;

    @Builder.Default
    @OneToMany(mappedBy = "owner")
    List<Card> cards = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender")
    List<CardSuggestion> outgoingSuggestions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recipient")
    List<CardSuggestion> incomingSuggestions = new ArrayList<>();

    @Builder.Default
    boolean active = true;

    public void addCard(Card card) {
        if (card != null) {
            this.cards.add(card);
            card.setOwner(this);
        }
    }

    public Learner(User user, Language language, CEFR selfReportedLevel) {
        this.user = user;
        this.language = language;
        this.selfReportedLevel = selfReportedLevel;
        this.active = true;
    }
}
