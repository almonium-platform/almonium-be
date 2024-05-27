package linguarium.card.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

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
import linguarium.engine.translator.model.enums.Language;
import linguarium.user.core.model.entity.Learner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
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
    Long id;

    @Builder.Default
    UUID publicId = UUID.randomUUID();

    String entry;

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

    @OneToMany(mappedBy = "card")
    List<Example> examples;

    @OneToMany(mappedBy = "card")
    List<Translation> translations;

    @OneToMany(mappedBy = "card")
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
