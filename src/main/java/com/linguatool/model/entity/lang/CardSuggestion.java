package com.linguatool.model.entity.lang;

import com.linguatool.model.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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
    @JoinColumn(name = "card_id", referencedColumnName = "id",  updatable = false)
    Card card;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", updatable = false)
    User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id", updatable = false)
    User recipient;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime created;

    public CardSuggestion(User sender, User recipient, Card card) {
        this.sender = sender;
        this.recipient = recipient;
        this.card = card;
        this.created = LocalDateTime.now();
    }
}
