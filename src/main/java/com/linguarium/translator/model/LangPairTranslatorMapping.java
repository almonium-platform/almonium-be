package com.linguarium.translator.model;

import javax.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"source_lang_id", "target_lang_id", "translator_id"})
)
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(TranslatorMappingKey.class)
public class LangPairTranslatorMapping implements Serializable {

    @Id
    @Column(name = "source_lang_id")
    Long sourceLangId;

    @Id
    @Column(name = "target_lang_id")
    Long targetLangId;

    @Id
    @Column(name = "translator_id")
    Long translatorId;

    @ManyToOne
    @JoinColumn(name = "source_lang_id", referencedColumnName = "id", insertable = false, updatable = false)
    LanguageEntity sourceLang;

    @ManyToOne
    @JoinColumn(name = "target_lang_id", referencedColumnName = "id", insertable = false, updatable = false)
    LanguageEntity targetLang;

    @ManyToOne
    @JoinColumn(name = "translator_id", referencedColumnName = "id", insertable = false, updatable = false)
    Translator translator;

    int priority;
}
