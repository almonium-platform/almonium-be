package com.linguarium.card.model;

import com.linguarium.user.model.Learner;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
}
