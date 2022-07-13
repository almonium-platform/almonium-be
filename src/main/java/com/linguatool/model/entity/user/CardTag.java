package com.linguatool.model.entity.user;

import com.linguatool.model.entity.lang.Card;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
//@IdClass(CardTagPK.class)
@NoArgsConstructor
@Table(name = "card_tag")
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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

}
