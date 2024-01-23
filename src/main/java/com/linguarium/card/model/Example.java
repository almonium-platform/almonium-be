package com.linguarium.card.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column
    String example;

    @Column
    String translation;

    //TODO comment out
    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    Card card;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Example example = (Example) o;

        return Objects.equals(id, example.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
