package com.linguatool.model.entity.lang;

import com.linguatool.model.entity.lang.CardTag;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.Locale;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    Long id;

    @Column(name = "text")
    @ColumnTransformer(read = "LOWER(text)")
    String text;

    @OneToMany(mappedBy = "tag")
    Set<CardTag> cardTags;

    public Tag(String proposedName) {
        this.text = normalizeText(proposedName);
    }

    public static String normalizeText(String text) {
        return text.replaceAll("\\s", "_").toLowerCase(Locale.ROOT).trim();
    }

}
