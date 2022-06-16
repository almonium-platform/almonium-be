package com.linguatool.model.entity.user;

import com.linguatool.model.entity.lang.Card;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Locale;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    Long id;

    @Column(name = "text")
    String text;

//    @ManyToMany(mappedBy = "tags")
//    Set<User> users;
//
    @ManyToMany(mappedBy = "tags")
    Set<Card> cards;

    public String normalizeText() {
        return text.replaceAll("\\s", "_").toLowerCase(Locale.ROOT).trim();
    }

}
