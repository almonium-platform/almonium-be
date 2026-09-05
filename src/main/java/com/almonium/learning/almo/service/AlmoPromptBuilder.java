package com.almonium.learning.almo.service;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.learning.review.model.ConfusionEdge;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 11: the system prompt, built once per turn from the queue rather than the deck. The rules in it are the rules on
 * the design page and nowhere else: one language, short turns, no praise, a confusion named in one line.
 */
@Component
@RequiredArgsConstructor
public class AlmoPromptBuilder {
    private static final CEFR DEFAULT_LEVEL = CEFR.B1;

    private final AlmoLanguageCopy copy;

    public String system(Language language, CEFR level, List<LearningItem> queue, List<ConfusionEdge> confusions) {
        String languageName = englishName(language);
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are Almo, a conversation partner for someone learning ")
                .append(languageName)
                .append(" at CEFR level ")
                .append(Optional.ofNullable(level).orElse(DEFAULT_LEVEL))
                .append(". You are not a tutor and you do not run lessons.\n\n");
        prompt.append("Rules:\n");
        prompt.append("- Write only in ")
                .append(languageName)
                .append(", at the learner's level. Never switch language, even if the learner does.\n");
        prompt.append(
                "- Short turns: one to three sentences. Plain register. No praise, no exclamation marks, no emoji.\n");
        prompt.append("- End your turn with a question.\n");
        prompt.append("- Use the learner's words below where they fit naturally. Never list them, count them,"
                + " or mention that you are using them.\n");
        prompt.append("- If the learner misuses one member of a known confusion pair, name both members and contrast"
                + " them in a single sentence, then carry the conversation on. Never a separate correction block.\n");
        prompt.append("- Do not correct anything else unless it blocks understanding. No grammar explanations.\n\n");

        if (queue.isEmpty()) {
            prompt.append("The learner has not saved any words yet.\n\n");
        } else {
            prompt.append("The learner's words (entry - meaning):\n");
            queue.forEach(item -> prompt.append("- ").append(describe(item)).append('\n'));
            prompt.append('\n');
        }

        if (!confusions.isEmpty()) {
            prompt.append("Known confusions (the learner has mixed these up before):\n");
            confusions.forEach(edge -> prompt.append("- ")
                    .append(edge.getSourceItem().getEntry())
                    .append(" vs ")
                    .append(edge.getTargetItem().getEntry())
                    .append('\n'));
            prompt.append('\n');
        }

        prompt.append("Answer with a single JSON object and nothing else:\n");
        prompt.append("{\"reply\": string, the message you send, in ")
                .append(languageName)
                .append(";\n");
        prompt.append(" \"translation\": string, the same message in English;\n");
        prompt.append(
                " \"reply_words\": [{\"entry\": string, \"surface\": string}], the learner's words your reply uses,"
                        + " with the exact form they take in the reply;\n");
        prompt.append(" \"learner_words\": [{\"entry\": string, \"surface\": string}], the learner's words that appear,"
                + " used correctly, in the learner's most recent message, with the exact form written there;\n");
        prompt.append(" \"contrast\": [string, string] or null, both members of a confusion pair exactly as they appear"
                + " in your reply, only when the reply contrasts them;\n");
        prompt.append(" \"confusion\": {\"misused\": entry, \"intended\": entry} or null, the pair the learner mixed up"
                + " in their most recent message, by entry, only when they did}\n");
        return prompt.toString();
    }

    private String describe(LearningItem item) {
        String meaning = item.getTranslations().stream()
                .map(Translation::getTranslation)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse(Optional.ofNullable(item.getSelectedSense()).orElse(""));
        return meaning.isBlank() ? item.getEntry() : item.getEntry() + " - " + meaning;
    }

    /** The instructions are in English, so the language is named in English here; the copy uses its own name. */
    private String englishName(Language language) {
        String name =
                Locale.forLanguageTag(language.name().toLowerCase(Locale.ROOT)).getDisplayLanguage(Locale.ENGLISH);
        return name.isBlank() ? copy.ownName(language) : name;
    }
}
