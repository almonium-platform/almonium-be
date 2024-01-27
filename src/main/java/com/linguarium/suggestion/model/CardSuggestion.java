package com.linguarium.suggestion.model;

import com.linguarium.card.model.Card;
import com.linguarium.user.model.Learner;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"card_id", "sender_id", "recipient_id"})
)
public class CardSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "card_id", referencedColumnName = "id", updatable = false)
    Card card;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", updatable = false)
    Learner sender;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id", updatable = false)
    Learner recipient;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime created;

    public CardSuggestion(Learner sender, Learner recipient, Card card) {
        this.sender = sender;
        this.recipient = recipient;
        this.card = card;
        this.created = LocalDateTime.now();
    }
}
