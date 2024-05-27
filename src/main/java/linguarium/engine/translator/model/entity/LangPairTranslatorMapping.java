package linguarium.engine.translator.model.entity;

import static lombok.AccessLevel.PRIVATE;

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
import linguarium.engine.translator.model.entity.pk.TranslatorMappingKey;
import linguarium.engine.translator.model.enums.Language;
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
