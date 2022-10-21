package com.linguatool.model.entity.lang;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"lang_from_id", "lang_to_id", "translator_id"})
)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@IdClass(LangPairTranslatorKey.class)
public class LangPairTranslator implements Serializable {

    @Id
    @Column(name = "lang_from_id")
    Long langFromId;

    @Id
    @Column(name = "lang_to_id")
    Long langToId;

    @Id
    @Column(name = "translator_id")
    Long translatorId;

    @ManyToOne
    @JoinColumn(name = "lang_from_id", referencedColumnName = "id", insertable = false, updatable = false)
    LanguageEntity langFrom;

    @ManyToOne
    @JoinColumn(name = "lang_to_id", referencedColumnName = "id", insertable = false, updatable = false)
    LanguageEntity langTo;

    @ManyToOne
    @JoinColumn(name = "translator_id", referencedColumnName = "id", insertable = false, updatable = false)
    Translator translator;

    int priority;
}
