package com.almonium.learning.cover.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * Where to look on Commons for a book's cover art, and which of what is found to show a judge. A work's category is
 * a tree: illustrations sit two or three levels down under names that say so, scanned editions hold hundreds of text
 * pages, and films, recordings and monuments hang off the same root. Taking the first files in the tree's order
 * shows a judge a scanned volume's front matter and nothing else, so the walk goes to the illustration categories
 * first, every category is sampled evenly through its length, and the candidates are dealt out across categories.
 * Nothing here asks Commons anything; it only decides.
 */
public final class CoverSearch {
    /** What a category is likely to hold, by its name. */
    public enum Kind {
        /** Pictures made of or for the book: looked at first, deepest and most fully. */
        ILLUSTRATION,
        /** One edition, usually scanned page by page: sampled thinly, its own subcategories left alone. */
        EDITION,
        OTHER,
        /** Films, recordings, stagings, places, monuments: never opened. */
        SKIP
    }

    /** How a file's name speaks for or against it; a judge still sees the doubtful ones when nothing better exists. */
    public enum Promise {
        LIKELY,
        UNKNOWN,
        UNLIKELY
    }

    private static final Pattern SKIPPED = Pattern.compile(
            "(?i)\\b(films?|movies?|television|tv series|miniseries|audio|librivox|"
                    + "songs?|music|musicals?|operas?|ballets?|plays?|theat(?:re|er)|stage|performing arts|radio|video games?|games?|"
                    + "comics?|anime|manga|cosplay|museums?|plaques?|statues?|sculptures?|monuments?|memorials?|graves?|tombs?|"
                    + "routes?|places|locations|things named|named after|translators|books about|adaptations|parodies|"
                    + "decorative letters|headpieces|tailpieces|initials|ornaments|bindings|bookplates|stamps|coins|banknotes)\\b");
    private static final Pattern ILLUSTRATED = Pattern.compile("(?i)(illustrat|engraving|etching|woodcut|lithograph|"
            + "drawings?\\b|paintings?\\b|plates\\b|frontispiece|\\bin art\\b|iconograph|\\bprints\\b|watercolou?rs?)");
    private static final Pattern EDITION =
            Pattern.compile("(?i)(\\(.*\\b1[4-9]\\d\\d\\b.*\\)|\\b1[4-9]\\d\\d\\b|editions?\\b)");

    private static final Pattern RASTER = Pattern.compile("(?i)\\.(jpe?g|png)$");
    private static final Pattern LIKELY = Pattern.compile(
            "(?i)(illustrat|frontispi|engraving|etching|woodcut|lithograph|drawing|\\bplate\\b|\\bscene\\b|\\bby [A-Z])");
    private static final Pattern UNLIKELY = Pattern.compile("(?i)(title[ _-]?page|titlepage|titelblatt|titelseite|"
            + "page de titre|portada|frontcover|\\bcover\\b|\\bspine\\b|\\bbinding\\b|contents|\\bindex\\b|advert|colophon|"
            + "imprint|half[ _-]?title|endpaper|manuscript|autograph|\\bletter\\b|signature|portrait|photo|\\bgrave\\b|tomb|"
            + "plaque|statue|monument|\\bmap\\b|poster|playbill|program(?:me)?\\b|\\bfilm\\b|movie|screenshot|\\blogo\\b|"
            + "\\(page \\d+\\)|\\bpage[ _]\\d+|\\bp\\. ?\\d+|\\bseite[ _]\\d+|\\bpagina[ _]\\d+|\\.pdf|\\.djvu)");

    private CoverSearch() {}

    public static Kind classify(String category) {
        if (SKIPPED.matcher(category).find()) {
            return Kind.SKIP;
        }
        if (ILLUSTRATED.matcher(category).find()) {
            return Kind.ILLUSTRATION;
        }
        return EDITION.matcher(category).find() ? Kind.EDITION : Kind.OTHER;
    }

    /** A category found under another: pictures stay pictures however their subcategory is named. */
    public static Kind classify(String category, Kind parent) {
        Kind own = classify(category);
        return own == Kind.OTHER && parent == Kind.ILLUSTRATION ? Kind.ILLUSTRATION : own;
    }

    /** How far below a category of this kind the walk goes on. */
    public static int depthBelow(Kind kind) {
        return switch (kind) {
            case ILLUSTRATION -> 2;
            case OTHER -> 1;
            case EDITION, SKIP -> 0;
        };
    }

    /** How many of a category's files are worth describing. */
    public static int quota(Kind kind) {
        return switch (kind) {
            case ILLUSTRATION -> 60;
            case OTHER -> 30;
            case EDITION -> 16;
            case SKIP -> 0;
        };
    }

    public static boolean raster(String fileTitle) {
        return RASTER.matcher(fileTitle).find();
    }

    public static Promise promise(String fileTitle) {
        if (UNLIKELY.matcher(fileTitle).find()) {
            return Promise.UNLIKELY;
        }
        return LIKELY.matcher(fileTitle).find() ? Promise.LIKELY : Promise.UNKNOWN;
    }

    /**
     * Up to {@code limit} of a category's files: the promising names first, the unpromising last, each band taken at
     * even steps through its length. A scanned book's first pages are its title and contents; its plates are spread
     * through it.
     */
    public static List<String> sample(List<String> fileTitles, int limit) {
        List<String> picked = new ArrayList<>();
        for (Promise band : Promise.values()) {
            List<String> inBand = fileTitles.stream()
                    .filter(CoverSearch::raster)
                    .filter(title -> promise(title) == band)
                    .toList();
            picked.addAll(evenly(inBand, limit - picked.size()));
        }
        return picked;
    }

    /** At most {@code limit} items at even steps through the list, first and last included. */
    public static <T> List<T> evenly(List<T> items, int limit) {
        if (limit <= 0) {
            return List.of();
        }
        if (items.size() <= limit) {
            return items;
        }
        if (limit == 1) {
            return List.of(items.get(items.size() / 2));
        }
        List<T> picked = new ArrayList<>();
        for (int step = 0; step < limit; step++) {
            picked.add(items.get((int) Math.round(step * (items.size() - 1) / (double) (limit - 1))));
        }
        return picked;
    }

    /**
     * The candidates a judge is shown, at most {@code limit}: dealt one at a time from each group in turn, the
     * groups in the order they were found (the walk finds the best first), so one large category cannot crowd out
     * the rest. Inside a group the promising names lead.
     */
    public static <T> List<T> deal(List<T> items, Function<T, String> group, Function<T, String> name, int limit) {
        Map<String, List<T>> groups = new LinkedHashMap<>();
        for (T item : items) {
            groups.computeIfAbsent(group.apply(item), key -> new ArrayList<>()).add(item);
        }
        groups.values()
                .forEach(
                        members -> members.sort((a, b) -> promise(name.apply(a)).compareTo(promise(name.apply(b)))));
        List<T> dealt = new ArrayList<>();
        for (int round = 0; dealt.size() < limit; round++) {
            boolean any = false;
            for (List<T> members : groups.values()) {
                if (round < members.size() && dealt.size() < limit) {
                    dealt.add(members.get(round));
                    any = true;
                }
            }
            if (!any) {
                break;
            }
        }
        return dealt;
    }
}
