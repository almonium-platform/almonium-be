package com.linguarium.card.suggestion.model;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.card.core.model.Card;
import com.linguarium.user.core.model.Learner;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"card_id", "sender_id", "recipient_id"}))
@EntityListeners(AuditingEntityListener.class)
public class CardSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    Card card;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    Learner sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    Learner recipient;

    @CreatedDate
    LocalDateTime created;

    public CardSuggestion(Learner sender, Learner recipient, Card card) {
        this.sender = sender;
        this.recipient = recipient;
        this.card = card;
        this.created = LocalDateTime.now();
    }
}
