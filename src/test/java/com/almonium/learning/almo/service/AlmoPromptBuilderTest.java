package com.almonium.learning.almo.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.learning.review.model.ConfusionEdge;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class AlmoPromptBuilderTest {
    private final AlmoPromptBuilder builder = new AlmoPromptBuilder(new AlmoLanguageCopy());

    @Test
    void namesTheLanguageLevelWordsAndConfusions() {
        LearningItem vornehmen = item("sich vornehmen", "to set out to");
        LearningItem besitzen = item("besitzen", "to own");
        LearningItem beherrschen = item("beherrschen", "to master");
        ConfusionEdge edge = ConfusionEdge.builder()
                .sourceItem(beherrschen)
                .targetItem(besitzen)
                .build();

        String prompt = builder.system(Language.DE, CEFR.B2, List.of(vornehmen, besitzen, beherrschen), List.of(edge));

        assertThat(prompt)
                .contains("learning German at CEFR level B2")
                .contains("Write only in German")
                .contains("- sich vornehmen - to set out to")
                .contains("- beherrschen vs besitzen")
                .contains("\"reply\"")
                .contains("\"learner_words\"")
                .contains("\"confusion\"");
    }

    @Test
    void wordWithoutMeaningIsListedBare() {
        LearningItem bare = LearningItem.builder().entry("dennoch").build();

        assertThat(builder.system(Language.DE, null, List.of(bare), List.of()))
                .contains("- dennoch\n")
                .contains("level B1");
    }

    @Test
    void emptyQueueIsSaidPlainly() {
        assertThat(builder.system(Language.FR, CEFR.A2, List.of(), List.of()))
                .contains("has not saved any words yet")
                .doesNotContain("Known confusions");
    }

    private static LearningItem item(String entry, String meaning) {
        LearningItem item = LearningItem.builder().entry(entry).build();
        List<Translation> translations = new ArrayList<>();
        translations.add(Translation.builder().translation(meaning).build());
        item.setTranslations(translations);
        return item;
    }
}
