package com.almonium.learning.rhythm.dto.request;

import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * @param language the language the bar belongs to; the target is a commitment to one language, not to the account
 * @param target days per week the learner asks of themselves: {@code null} when never chosen, {@code 0} for the
 *     deliberate "no target", otherwise 2, 3, 5 or 7
 */
public record RhythmTargetRequest(@NotNull Language language, Integer target) {
    private static final Set<Integer> ALLOWED_TARGETS = Set.of(0, 2, 3, 5, 7);

    @AssertTrue(message = "Weekly target must be null, 0, 2, 3, 5 or 7")
    public boolean isTargetAllowed() {
        return target == null || ALLOWED_TARGETS.contains(target);
    }
}
