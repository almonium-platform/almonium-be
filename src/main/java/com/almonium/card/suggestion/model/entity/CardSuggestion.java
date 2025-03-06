package com.almonium.card.suggestion.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.model.entity.Card;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
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
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(
        name = "card_suggestion",
        uniqueConstraints = @UniqueConstraint(columnNames = {"card_id", "sender_id", "recipient_id"}))
@EntityListeners(AuditingEntityListener.class)
public class CardSuggestion {

    @Id
    @UuidV7
    UUID id;

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
    Instant createdAt;

    public CardSuggestion(Learner sender, Learner recipient, Card card) {
        this.sender = sender;
        this.recipient = recipient;
        this.card = card;
        this.createdAt = Instant.now();
    }
}
