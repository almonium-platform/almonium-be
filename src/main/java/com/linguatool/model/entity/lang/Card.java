package com.linguatool.model.entity.lang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linguatool.model.entity.user.User;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.linguatool.util.GeneralUtils.generateId;


@Entity
@Table(name = "card")
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

    @Column(name = "generated_id", nullable = false, unique = true)
    String hash;

    @Column
    String entry;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime updated;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime lastRepeat;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    User owner;

    @Type(type = "numeric_boolean")
    boolean irregularSpelling;

    @Type(type = "numeric_boolean")
    boolean activeLearning = true;

    @Type(type = "numeric_boolean")
    boolean falseFriend;

    @Type(type = "numeric_boolean")
    boolean irregularPlural;

    @Type(type = "numeric_boolean")
    boolean learnt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lang_id", referencedColumnName = "id")
    LanguageEntity language;

    @OneToMany(mappedBy = "card", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Example> examples;

    @OneToMany(mappedBy = "card", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<CardTag> cardTags;

    @OneToMany(mappedBy = "card", cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<CardSuggestion> suggestions;

    public void addExample(Example example) {
        if (example != null) {
            this.examples.add(example);
            example.setCard(this);
        }
    }

    public void addCardTag(CardTag cardTag) {
        if (cardTag != null) {
            this.cardTags.add(cardTag);
        }
    }

    public void removeCardTag(CardTag cardTag) {
        if (cardTag != null) {
            this.cardTags.remove(cardTag);
        }
    }

    public void removeExample(Example example) {
        if (example != null) {
            this.examples.remove(example);
            example.setCard(null);
        }
    }

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "card", cascade = CascadeType.PERSIST) //check
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Translation> translations;

    private String notes;

    private String source;

    private int iteration = 0;

    private int priority = 2;

    private String ipa;

    private int frequency;

    String wordFamily;

    String hardIndices;

    public void assignTranslations() {
        for (Translation translation : translations) {
            translation.setCard(this);
        }
    }

    @PrePersist
    void setDefault() {
        if (this.hash == null) {
            this.hash = generateId();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return irregularSpelling == card.irregularSpelling && activeLearning == card.activeLearning && falseFriend == card.falseFriend && irregularPlural == card.irregularPlural && learnt == card.learnt && iteration == card.iteration && priority == card.priority && frequency == card.frequency && id.equals(card.id) && hash.equals(card.hash) && entry.equals(card.entry) && Objects.equals(created, card.created) && Objects.equals(updated, card.updated) && Objects.equals(lastRepeat, card.lastRepeat) && owner.equals(card.owner) && language.equals(card.language) && Objects.equals(examples, card.examples) && Objects.equals(cardTags, card.cardTags) && Objects.equals(suggestions, card.suggestions) && translations.equals(card.translations) && Objects.equals(notes, card.notes) && Objects.equals(source, card.source) && Objects.equals(ipa, card.ipa) && Objects.equals(wordFamily, card.wordFamily) && Objects.equals(hardIndices, card.hardIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hash, entry, created, updated, lastRepeat, owner, irregularSpelling, activeLearning, falseFriend, irregularPlural, learnt, language, examples, cardTags, suggestions, translations, notes, source, iteration, priority, ipa, frequency, wordFamily, hardIndices);
    }
}

