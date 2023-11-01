package com.linguarium.card.model;

import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    UUID publicId = UUID.randomUUID();

    @Column
    String entry;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    Learner owner;

    boolean activeLearning = true;
    boolean irregularSpelling;
    boolean falseFriend;
    boolean irregularPlural;
    boolean learnt;

    @NotNull
    @Enumerated(EnumType.STRING)
    Language language;

    @OneToMany(mappedBy = "card", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Example> examples;

    @OneToMany(mappedBy = "card", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Translation> translations;

    @OneToMany(mappedBy = "card", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<CardTag> cardTags;

    private String notes;
    private int iteration = 0;
    private int priority = 2;
    private int frequency;

    public void removeCardTag(CardTag cardTag) {
        if (cardTag != null) {
            this.cardTags.remove(cardTag);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
