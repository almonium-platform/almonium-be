package com.linguarium.translator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"source_lang", "target_lang", "translator_id"}))
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
