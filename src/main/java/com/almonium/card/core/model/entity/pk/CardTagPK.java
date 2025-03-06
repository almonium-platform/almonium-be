package com.almonium.card.core.model.entity.pk;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class CardTagPK {
    private UUID cardId;
    private UUID tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardTagPK that = (CardTagPK) o;
        return cardId.equals(that.cardId) && tagId.equals(that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardId, tagId);
    }
}
