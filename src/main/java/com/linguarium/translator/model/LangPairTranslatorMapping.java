package com.linguarium.translator.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"source_lang", "target_lang", "translator_id"})
)
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(TranslatorMappingKey.class)
public class LangPairTranslatorMapping implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "source_lang")
    Language sourceLang;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "target_lang")
    Language targetLang;

    @Id
    @Column(name = "translator_id")
    Long translatorId;

    @ManyToOne
    @JoinColumn(name = "translator_id", referencedColumnName = "id", insertable = false, updatable = false)
    Translator translator;

    int priority;
}
