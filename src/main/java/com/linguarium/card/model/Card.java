package com.linguarium.card.model;

import static lombok.AccessLevel.PRIVATE;

import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "public_id", nullable = false, unique = true)
    @Builder.Default
    UUID publicId = UUID.randomUUID();

    @Column
    String entry;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    LocalDateTime created;

    @LastModifiedDate
    LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    Learner owner;

    @Builder.Default
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

    @Builder.Default
    private int iteration = 0;

    @Builder.Default
    private int priority = 2;

    private int frequency;

    public void removeCardTag(CardTag cardTag) {
        if (cardTag != null) {
            cardTags.remove(cardTag);
        }
    }
}
