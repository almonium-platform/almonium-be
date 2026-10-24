package com.almonium.analyzer.analyzer.mapper;

import com.almonium.analyzer.analyzer.dto.IpaSegmentResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Where the varieties' transcriptions of one word part (design 5a): each transcription comes back cut into segments
 * that concatenate to it exactly, those that differ from another variety's flagged.
 *
 * <p>A transcription is cut into phonemes, not characters. IPA writes one symbol per sound, so a phoneme is a symbol
 * with the marks that belong to it (diacritics, length, aspiration, a tie bar and the symbol it ties), except for the
 * few sounds a language writes as two untied symbols: its diphthongs and affricates, listed per language below. A
 * vowel marked non-syllabic (aɪ̯) joins the one before it in any language. A language without a list is compared
 * symbol by symbol, which tints a changed diphthong by its changed half instead of whole.
 *
 * <p>Before comparing, spellings of one sound are made one (ɹ and r, ɝ and ɜːr, t͡ʃ and tʃ) and syllable dots are
 * ignored. A variety may record several transcriptions (niche: British ˈniːʃ or ˈnɪtʃ), and those are its own
 * alternatives, not a parting, so a transcription is compared only with other varieties': in each, with the one closest
 * to it. Two are aligned by edit distance, sounds only; a phoneme substituted or inserted is flagged in the one it is
 * in. A variety recording the same alternatives as another (both say ˈniːʃ and ˈnɪtʃ) therefore parts nowhere.
 * Stress is then compared by the sound each mark stands before, and left out between two transcriptions when one of
 * them marks none: a source that omits stress has not said it differs.
 */
public final class TranscriptionDiff {
    /** Closing diphthongs and affricates. Centring ones (ɪə) stay two, so British ɪə against American ɪɹ shares the ɪ. */
    private static final Map<String, Set<String>> UNITS = Map.of(
            "en", Set.of("eɪ", "aɪ", "ɔɪ", "oʊ", "əʊ", "aʊ", "tʃ", "dʒ"),
            "de", Set.of("aɪ", "aʊ", "ɔʏ", "ts", "pf", "tʃ"));

    /** One sound by two notations; the value is what both compare as, phoneme by phoneme. */
    private static final Map<String, List<String>> SAME_SOUND = Map.of(
            "ɹ", List.of("r"),
            "ɡ", List.of("g"),
            "ɝ", List.of("ɜː", "r"),
            "ɝː", List.of("ɜː", "r"),
            "ɚ", List.of("ə", "r"));

    private static final Pattern ATTACHES = Pattern.compile("[\\p{M}ːˑʰʷʲˠˤ˞ʼ]");
    /** Tie bars and the non-syllabic mark say how a unit is written, not how it sounds: t͡ʃ is tʃ, aɪ̯ is aɪ. */
    private static final Pattern NOTATION = Pattern.compile("[\u0361\u035C\u032F]");

    private static final Set<String> STRESS = Set.of("ˈ", "ˌ");
    private static final Set<String> IGNORED = Set.of(".", " ", "/", "[", "]");
    private static final char NON_SYLLABIC = '\u032F';
    private static final int INDEL = 2;
    private static final int SUBSTITUTION = 3;

    private TranscriptionDiff() {}

    /** Segments for each transcription, every one taken as its own variety. */
    public static List<List<IpaSegmentResponse>> segments(List<String> transcriptions, String language) {
        return segments(
                transcriptions,
                IntStream.range(0, transcriptions.size())
                        .mapToObj(String::valueOf)
                        .toList(),
                language);
    }

    /**
     * Segments for each transcription, in order, against the closest transcription of each other variety; a null
     * transcription gets null.
     */
    public static List<List<IpaSegmentResponse>> segments(
            List<String> transcriptions, List<String> varieties, String language) {
        Set<String> units = UNITS.getOrDefault(language == null ? "" : language.toLowerCase(), Set.of());
        List<List<String>> phonemes = transcriptions.stream()
                .map(transcription -> transcription == null ? null : phonemes(transcription, units))
                .toList();
        List<boolean[]> differs = phonemes.stream()
                .map(line -> line == null ? null : new boolean[line.size()])
                .toList();
        Map<String, List<Integer>> byVariety = new LinkedHashMap<>();
        for (int index = 0; index < phonemes.size(); index++) {
            if (phonemes.get(index) != null) {
                byVariety
                        .computeIfAbsent(varieties.get(index), variety -> new ArrayList<>())
                        .add(index);
            }
        }
        for (int index = 0; index < phonemes.size(); index++) {
            if (phonemes.get(index) == null) continue;
            List<String> line = phonemes.get(index);
            for (Map.Entry<String, List<Integer>> variety : byVariety.entrySet()) {
                if (variety.getKey().equals(varieties.get(index))) continue;
                boolean[] closest = null;
                int closestCost = Integer.MAX_VALUE;
                for (int other : variety.getValue()) {
                    boolean[] flags = new boolean[line.size()];
                    int cost = compare(
                            line,
                            phonemes.get(other),
                            flags,
                            new boolean[phonemes.get(other).size()]);
                    if (cost < closestCost) {
                        closest = flags;
                        closestCost = cost;
                    }
                }
                for (int phoneme = 0; phoneme < line.size(); phoneme++) {
                    differs.get(index)[phoneme] |= closest[phoneme];
                }
            }
        }
        List<List<IpaSegmentResponse>> result = new ArrayList<>();
        for (int index = 0; index < phonemes.size(); index++) {
            result.add(phonemes.get(index) == null ? null : merge(phonemes.get(index), differs.get(index)));
        }
        return result;
    }

    /** Symbols with what attaches to them, then the language's two-symbol sounds joined. */
    static List<String> phonemes(String transcription, Set<String> units) {
        List<String> symbols = new ArrayList<>();
        transcription.codePoints().mapToObj(Character::toString).forEach(symbol -> {
            int last = symbols.size() - 1;
            boolean tied = last >= 0
                    && (symbols.get(last).endsWith("͡") || symbols.get(last).endsWith("͜"));
            if (last >= 0 && (ATTACHES.matcher(symbol).matches() || tied) && !IGNORED.contains(symbols.get(last))) {
                symbols.set(last, symbols.get(last) + symbol);
            } else {
                symbols.add(symbol);
            }
        });
        List<String> joined = new ArrayList<>();
        for (int index = 0; index < symbols.size(); index++) {
            String next = index + 1 < symbols.size() ? symbols.get(index + 1) : null;
            if (next != null && (units.contains(symbols.get(index) + next) || next.indexOf(NON_SYLLABIC) >= 0)) {
                joined.add(symbols.get(index) + next);
                index++;
            } else {
                joined.add(symbols.get(index));
            }
        }
        return joined;
    }

    /** One step of the comparison: the phoneme it reads as, and which phoneme of the line it came from. */
    private record Key(String sound, int phoneme) {}

    /** A stress mark, as the phoneme it stands at and the key it sits before (the key count when it ends the line). */
    private record Mark(String mark, int phoneme, int before) {}

    private static List<Key> keys(List<String> phonemes) {
        List<Key> keys = new ArrayList<>();
        for (int index = 0; index < phonemes.size(); index++) {
            String phoneme = phonemes.get(index);
            if (IGNORED.contains(phoneme) || STRESS.contains(phoneme)) continue;
            String plain = NOTATION.matcher(phoneme).replaceAll("");
            for (String sound : SAME_SOUND.getOrDefault(plain, List.of(plain))) {
                keys.add(new Key(sound, index));
            }
        }
        return keys;
    }

    private static List<Mark> marks(List<String> phonemes, List<Key> keys) {
        List<Mark> marks = new ArrayList<>();
        int next = 0;
        for (int index = 0; index < phonemes.size(); index++) {
            while (next < keys.size() && keys.get(next).phoneme() < index) next++;
            if (STRESS.contains(phonemes.get(index))) marks.add(new Mark(phonemes.get(index), index, next));
        }
        return marks;
    }

    /**
     * Edit-distance alignment of two lines' sounds; what is substituted is flagged in both, what is inserted in its
     * own. A substitution costs less than an insertion and a deletion together but more than either, so a sound both
     * lines share is never traded away to shift a run (ɡəˈrɑːʒ against ˈɡærɑːʒ keeps the ɡ matched). Stress is
     * compared after, against the alignment: a mark is flagged unless the other line has the same mark before the
     * sound this one's stands before, so a stress that moves a syllable shows even where the sounds around it agree.
     * When either line marks no stress at all, stress is left out. Returns the alignment's cost, a flagged stress
     * mark counting as one more step, so the closest of a variety's alternatives can be picked.
     */
    private static int compare(
            List<String> first, List<String> second, boolean[] firstDiffers, boolean[] secondDiffers) {
        List<Key> a = keys(first);
        List<Key> b = keys(second);
        int[][] cost = new int[a.size() + 1][b.size() + 1];
        for (int i = 0; i <= a.size(); i++) cost[i][0] = i * INDEL;
        for (int j = 0; j <= b.size(); j++) cost[0][j] = j * INDEL;
        for (int i = 1; i <= a.size(); i++) {
            for (int j = 1; j <= b.size(); j++) {
                cost[i][j] = Math.min(
                        cost[i - 1][j - 1] + step(a.get(i - 1), b.get(j - 1)),
                        Math.min(cost[i - 1][j], cost[i][j - 1]) + INDEL);
            }
        }
        int[] aToB = new int[a.size() + 1];
        int[] bToA = new int[b.size() + 1];
        Arrays.fill(aToB, -1);
        Arrays.fill(bToA, -1);
        aToB[a.size()] = b.size();
        bToA[b.size()] = a.size();
        int i = a.size();
        int j = b.size();
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && cost[i][j] == cost[i - 1][j - 1] + step(a.get(i - 1), b.get(j - 1))) {
                if (step(a.get(i - 1), b.get(j - 1)) != 0) {
                    firstDiffers[a.get(i - 1).phoneme()] = true;
                    secondDiffers[b.get(j - 1).phoneme()] = true;
                }
                aToB[i - 1] = j - 1;
                bToA[j - 1] = i - 1;
                i--;
                j--;
            } else if (i > 0 && cost[i][j] == cost[i - 1][j] + INDEL) {
                firstDiffers[a.get(--i).phoneme()] = true;
            } else {
                secondDiffers[b.get(--j).phoneme()] = true;
            }
        }
        List<Mark> firstMarks = marks(first, a);
        List<Mark> secondMarks = marks(second, b);
        int distance = cost[a.size()][b.size()];
        if (firstMarks.isEmpty() || secondMarks.isEmpty()) return distance;
        return distance
                + flagUnmatched(firstMarks, secondMarks, aToB, firstDiffers)
                + flagUnmatched(secondMarks, firstMarks, bToA, secondDiffers);
    }

    private static int flagUnmatched(List<Mark> marks, List<Mark> others, int[] aligned, boolean[] differs) {
        int unmatched = 0;
        for (Mark mark : marks) {
            int partner = aligned[mark.before()];
            boolean matched = partner >= 0
                    && others.stream()
                            .anyMatch(other ->
                                    other.before() == partner && other.mark().equals(mark.mark()));
            if (!matched) {
                differs[mark.phoneme()] = true;
                unmatched++;
            }
        }
        return unmatched;
    }

    private static int step(Key a, Key b) {
        return a.sound().equals(b.sound()) ? 0 : SUBSTITUTION;
    }

    private static List<IpaSegmentResponse> merge(List<String> phonemes, boolean[] differs) {
        List<IpaSegmentResponse> segments = new ArrayList<>();
        StringBuilder text = new StringBuilder();
        boolean state = false;
        for (int index = 0; index < phonemes.size(); index++) {
            if (index > 0 && differs[index] != state) {
                segments.add(new IpaSegmentResponse(text.toString(), state));
                text.setLength(0);
            }
            state = differs[index];
            text.append(phonemes.get(index));
        }
        if (!text.isEmpty()) segments.add(new IpaSegmentResponse(text.toString(), state));
        return segments;
    }
}
