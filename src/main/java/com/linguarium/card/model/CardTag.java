package com.linguarium.card.model;

import com.linguarium.user.model.Learner;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardTag {

    @EmbeddedId
    CardTagPK id = new CardTagPK();

    @ManyToOne
    @MapsId("cardId")
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    Card card;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    Tag tag;

    @ManyToOne
    @JoinColumn(name = "learner_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Learner learner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardTag cardTag = (CardTag) o;

        return Objects.equals(id, cardTag.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
