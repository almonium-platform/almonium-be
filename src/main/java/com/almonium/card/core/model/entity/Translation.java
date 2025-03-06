package com.almonium.card.core.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.uuid.UuidV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = PRIVATE)
public class Translation {

    @Id
    @UuidV7
    UUID id;

    String translation;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    Card card;
}
