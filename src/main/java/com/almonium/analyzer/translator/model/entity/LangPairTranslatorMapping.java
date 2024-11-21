package com.almonium.analyzer.translator.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.entity.pk.TranslatorMappingKey;
import com.almonium.analyzer.translator.model.enums.Language;
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
@Table(
        name = "lang_pair_translator_mapping",
        uniqueConstraints = @UniqueConstraint(columnNames = {"source_lang", "target_lang", "translator_id"}))
@FieldDefaults(level = PRIVATE)
@IdClass(TranslatorMappingKey.class)
public class LangPairTranslatorMapping {
    @Id
    @Enumerated(EnumType.STRING)
    Language sourceLang;

    @Id
    @Enumerated(EnumType.STRING)
    Language targetLang;

    @Id
    @Column(name = "translator_id")
    Long translatorId;

    @ManyToOne
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    Translator translator;

    int priority;
}
