package com.almonium.learning.almo.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Optional;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * What the model is asked to hand back for one turn. The reply is the only part that becomes a message; the rest is
 * bookkeeping the client and the scheduler read, and none of it is shown as a correction block.
 *
 * @param reply the bubble, in the target language
 * @param translation the same line in the learner's language, swapped in while a bubble is held
 * @param replyWords queue words the reply used, with the form they take in it
 * @param learnerWords queue words the learner used correctly in their last message
 * @param contrast both members of the confusion pair the reply contrasted, as written in the reply
 * @param confusion the pair the learner mixed up, by queue entry, when they did
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AlmoDraft(
        String reply,
        String translation,
        List<WordUse> replyWords,
        List<WordUse> learnerWords,
        List<String> contrast,
        Confusion confusion) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record WordUse(String entry, String surface) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Confusion(String misused, String intended) {}

    /** The model is allowed to leave a list out; nobody downstream should have to ask twice. */
    public AlmoDraft normalized() {
        return new AlmoDraft(
                Optional.ofNullable(reply).map(String::trim).orElse(""),
                Optional.ofNullable(translation).map(String::trim).orElse(""),
                clean(replyWords),
                clean(learnerWords),
                contrast == null || contrast.size() != 2 ? List.of() : contrast,
                confusion != null && confusion.misused() != null && confusion.intended() != null ? confusion : null);
    }

    private static List<WordUse> clean(List<WordUse> uses) {
        if (uses == null) {
            return List.of();
        }
        return uses.stream()
                .filter(use ->
                        use != null && use.surface() != null && !use.surface().isBlank())
                .toList();
    }
}
