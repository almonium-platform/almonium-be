package com.almonium.learning.play.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.play.dto.response.HigherLowerBest;
import com.almonium.learning.play.dto.response.HigherLowerRunResult;
import com.almonium.learning.play.dto.response.HigherLowerWord;
import com.almonium.learning.play.repository.HigherLowerBestStore;
import com.almonium.learning.play.repository.HigherLowerWordStore;
import com.almonium.user.core.model.entity.User;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Higher or Lower: two words, say whether the second turns up in print more or less often than the first. The client
 * gets one random batch of words per run and picks the pairs from it; the server keeps only each member's best run
 * per language. Nothing here touches the deck or review.
 */
@Service
@RequiredArgsConstructor
public class HigherLowerService {
    /** Enough words that a run never exhausts the batch, and dense enough that every gap finds a partner. */
    static final int BATCH_SIZE = 600;

    private final HigherLowerWordStore wordStore;
    private final HigherLowerBestStore bestStore;

    /**
     * A fresh batch of words in {@code language}, each with its meaning in {@code fluent}. Without a fluent
     * language, meanings are English, or Ukrainian for English itself.
     */
    @Transactional(readOnly = true)
    public List<HigherLowerWord> words(Language language, Language fluent) {
        Language meanings =
                fluent != null && fluent != language ? fluent : language == Language.EN ? Language.UK : Language.EN;
        return wordStore.sample(lexiconCode(language), lexiconCode(meanings), BATCH_SIZE);
    }

    @Transactional(readOnly = true)
    public List<HigherLowerBest> bests(User user) {
        return bestStore.bests(user.getId());
    }

    /** Records a finished run; the stored best only ever grows. */
    @Transactional
    public HigherLowerRunResult record(User user, Language language, int length) {
        Integer previous = bestStore.lockBest(user.getId(), language).orElse(null);
        bestStore.keepLonger(user.getId(), language, length);
        return new HigherLowerRunResult(previous == null ? length : Math.max(previous, length), previous);
    }

    private static String lexiconCode(Language language) {
        return language.name().toLowerCase(Locale.ROOT);
    }
}
