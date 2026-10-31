package com.almonium.learning.lexicon.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Carries the keys of an entry's senses over to the senses of its rewrite, so a card saved from a sense still names
 * it after the entry is written again. Senses are compared across all lexemes of the headword, because a rewrite may
 * move a sense to another lexeme (Band split by gender). The evidence a sense cites weighs most, then the same
 * definition, the same gloss in a reader's language, shared definition words, and the same lemma and part of speech;
 * pairs are taken best first, each sense at most once. A new sense nothing resembles enough gets a fresh key; an old
 * sense nothing took is retired, pointing at the new sense it most resembles, if any resembles it at all.
 */
public final class SenseMatcher {

    /** Enough to say two senses are the same meaning: one shared citation, or one shared gloss and some wording. */
    static final double SAME = 3.0;
    /** Enough to say a retired sense lives on, roughly, in another. */
    static final double NEAR = 1.5;

    private SenseMatcher() {}

    /** What a sense is compared by. {@code glosses} maps a reader's language to the sense's gloss in it. */
    public record Candidate(
            String lemma, String pos, String definition, Map<String, String> glosses, List<String> cited) {}

    /** The key of each new sense, in order, and each retired key with its nearest successor (null for none). */
    public record Result(List<UUID> keys, Map<UUID, UUID> retired) {}

    public static Result match(Map<UUID, Candidate> old, List<Candidate> fresh) {
        record Pair(UUID key, int index, double score) {}
        List<Pair> pairs = new ArrayList<>();
        for (Map.Entry<UUID, Candidate> standing : old.entrySet()) {
            for (int i = 0; i < fresh.size(); i++) {
                double score = score(standing.getValue(), fresh.get(i));
                if (score > 0) {
                    pairs.add(new Pair(standing.getKey(), i, score));
                }
            }
        }
        pairs.sort(Comparator.comparingDouble(Pair::score).reversed());
        UUID[] keys = new UUID[fresh.size()];
        Set<UUID> taken = new HashSet<>();
        for (Pair pair : pairs) {
            if (pair.score() >= SAME && keys[pair.index()] == null && taken.add(pair.key())) {
                keys[pair.index()] = pair.key();
            }
        }
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == null) {
                keys[i] = UUID.randomUUID();
            }
        }
        Map<UUID, UUID> retired = new LinkedHashMap<>();
        for (UUID key : old.keySet()) {
            if (taken.contains(key)) {
                continue;
            }
            UUID nearest = pairs.stream()
                    .filter(pair -> pair.key().equals(key) && pair.score() >= NEAR)
                    .findFirst()
                    .map(pair -> keys[pair.index()])
                    .orElse(null);
            retired.put(key, nearest);
        }
        return new Result(Arrays.asList(keys), retired);
    }

    static double score(Candidate a, Candidate b) {
        double score = 0;
        Set<String> cited = new HashSet<>(a.cited());
        cited.retainAll(b.cited());
        if (!cited.isEmpty()) {
            Set<String> all = new HashSet<>(a.cited());
            all.addAll(b.cited());
            score += 4.0 * cited.size() / all.size();
        }
        if (normal(a.definition()).equals(normal(b.definition()))) {
            score += 10;
        } else {
            score += 3.0 * jaccard(words(a.definition()), words(b.definition()));
        }
        for (Map.Entry<String, String> gloss : a.glosses().entrySet()) {
            String other = b.glosses().get(gloss.getKey());
            if (other == null) {
                continue;
            }
            if (normal(gloss.getValue()).equals(normal(other))) {
                score += 2;
            } else if (jaccard(words(gloss.getValue()), words(other)) > 0) {
                score += 1;
            }
        }
        if (a.lemma().equals(b.lemma()) && a.pos().equals(b.pos())) {
            score += 0.5;
        }
        return score;
    }

    private static String normal(String text) {
        return text == null ? "" : text.toLowerCase(Locale.ROOT).strip();
    }

    /** The words of a text of three letters or more, so articles and particles do not make two senses alike. */
    private static Set<String> words(String text) {
        return Arrays.stream(normal(text).split("[^\\p{L}\\p{M}]+"))
                .filter(word -> word.codePointCount(0, word.length()) >= 3)
                .collect(Collectors.toSet());
    }

    private static double jaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() || b.isEmpty()) {
            return 0;
        }
        Set<String> shared = new HashSet<>(a);
        shared.retainAll(b);
        Set<String> all = new HashSet<>(a);
        all.addAll(b);
        return (double) shared.size() / all.size();
    }
}
