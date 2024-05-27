package linguarium.engine.analyzer.dto;

import static lombok.AccessLevel.PRIVATE;

import linguarium.card.core.dto.CardDto;
import linguarium.engine.analyzer.model.CEFR;
import linguarium.engine.translator.dto.TranslationCardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class AnalysisDto {
    Double frequency;
    CEFR cefr;
    String[] lemmas;
    String[] posTags;
    String[] adjectives;
    String[] nouns;
    CardDto[] foundCards;
    String[] homophones;
    String[] family;
    String[] syllables;
    Boolean isProper;
    Boolean isForeignWord;
    Boolean isPlural;
    TranslationCardDto translationCards;
}
