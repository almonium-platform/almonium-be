package com.almonium.learning.cover.client;

import com.almonium.config.properties.CoverProperties;
import com.almonium.learning.cover.service.CoverSearch;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

/**
 * What Wikimedia holds for a work: the image Wikidata gives it and a sample of its Commons category tree, each file
 * with the licence facts its page carries. Reading only; which categories to open and which files to keep is
 * {@link CoverSearch}'s decision, and nothing here judges a picture.
 */
@Component
@RequiredArgsConstructor
public class CommonsClient {
    private static final String WIKIDATA = "https://www.wikidata.org/w/api.php";
    private static final String COMMONS = "https://commons.wikimedia.org/w/api.php";
    /** A width Commons serves ready-made: enough for a cover on a dense screen, light enough for a shelf of them. */
    private static final int WIDTH = 500;

    /** The most file names read from one category; a scanned volume has hundreds, and they are sampled. */
    private static final int CATEGORY_FILES = 500;

    private static final int SUBCATEGORIES = 50;
    /** How many categories of one work are opened, the best first. */
    private static final int MAX_CATEGORIES = 30;
    /** How many files of one work are described; each batch of twenty is a request. */
    private static final int MAX_FILES = 420;

    private static final int SEARCH_HITS = 30;
    private static final int BATCH = 20;
    public static final String WIKIDATA_IMAGE = "Wikidata's image for the work";
    public static final String SEARCH = "Commons search";
    /** Encyclopedias asked for the images beside a work's article, in this order, and how many of them. */
    private static final List<String> ARTICLE_LANGUAGES = List.of(
            "en", "de", "fr", "es", "it", "pt", "ru", "uk", "pl", "nl", "sv", "da", "no", "fi", "hu", "cs", "el");

    private static final int ARTICLES = 4;
    private static final Pattern LIFESPAN = Pattern.compile("\\b(1[4-9]\\d\\d)\\s*[–—-]\\s*(1[4-9]\\d\\d)\\b");
    private static final Pattern YEAR_OF = Pattern.compile("^\\+?(\\d{4})-");
    private static final Pattern NOT_A_PERSON = Pattern.compile(
            "(?i)(unknown|anonym|unidentified|various|library|museum|archive|university|publisher|press\\b|company|"
                    + "\\bco\\.|\\bsons\\b|internet|google|flickr|https?:|\\d)");
    private static final Pattern FILM_OR_STAGE =
            Pattern.compile("(?i)\\b(film|movie|opera|ballet|musical|television|series|album|song|game|play by)\\b");

    private final RestTemplate restTemplate;
    private final CoverProperties properties;
    /** An artist is looked up once; the same few illustrators sign most of a shelf's plates. */
    private final Map<String, Optional<Integer>> deathYears = new ConcurrentHashMap<>();

    /** A file on Commons with what its page says about it. */
    public record CommonsFile(
            String title,
            String pageUrl,
            String imageUrl,
            String mime,
            int width,
            int height,
            String artist,
            String licence,
            boolean copyrighted,
            List<String> hiddenCategories,
            /** The category the file was found in, which often says what it is. */
            String foundIn) {}

    /** A category waiting to be opened: its kind decides when, and its depth how much further the walk goes. */
    private record Waiting(String category, CoverSearch.Kind kind, int depthLeft) {}

    /**
     * The files a work's Wikidata item leads to: its own image, then a sample of its Commons category tree, the
     * illustration categories first (see {@link CoverSearch}).
     */
    public List<CommonsFile> filesFor(String wikidataId) {
        JsonNode entity = entity(wikidataId);
        JsonNode claims = entity.path("claims");
        Map<String, String> foundIn = new LinkedHashMap<>();
        values(claims, "P18").forEach(name -> foundIn.putIfAbsent("File:" + name, WIKIDATA_IMAGE));

        PriorityQueue<Waiting> waiting = new PriorityQueue<>(
                Comparator.comparing(Waiting::kind).thenComparing(Waiting::depthLeft, Comparator.reverseOrder()));
        Set<String> seen = new HashSet<>();
        for (String category : rootCategories(entity)) {
            if (seen.add(category)) {
                // the work's own category is opened whatever it is called, and its children looked at
                String root = category.replaceFirst("^Category:", "");
                CoverSearch.Kind kind = CoverSearch.classify(root) == CoverSearch.Kind.ILLUSTRATION
                        ? CoverSearch.Kind.ILLUSTRATION
                        : CoverSearch.Kind.OTHER;
                waiting.add(new Waiting(category, kind, Math.max(2, CoverSearch.depthBelow(kind))));
            }
        }
        int opened = 0;
        while (!waiting.isEmpty() && opened < MAX_CATEGORIES && foundIn.size() < MAX_FILES) {
            Waiting next = waiting.poll();
            opened++;
            String name = next.category().replaceFirst("^Category:", "");
            for (String file : CoverSearch.sample(
                    members(next.category(), "file", CATEGORY_FILES), CoverSearch.quota(next.kind()))) {
                if (foundIn.size() < MAX_FILES) {
                    foundIn.putIfAbsent(file, name);
                }
            }
            if (next.depthLeft() <= 0) {
                continue;
            }
            for (String child : members(next.category(), "subcat", SUBCATEGORIES)) {
                CoverSearch.Kind kind = CoverSearch.classify(child.replaceFirst("^Category:", ""), next.kind());
                if (kind != CoverSearch.Kind.SKIP && seen.add(child)) {
                    waiting.add(new Waiting(child, kind, Math.min(next.depthLeft() - 1, CoverSearch.depthBelow(kind))));
                }
            }
        }
        // what the encyclopedias' editors put beside the article: often the one period plate there is
        for (Map.Entry<String, String> article : articles(entity).entrySet()) {
            for (String file : articleImages(article.getKey(), article.getValue())) {
                if (foundIn.size() < MAX_FILES) {
                    foundIn.putIfAbsent(file, "Wikipedia article (" + article.getKey() + ")");
                }
            }
        }
        return describeAll(foundIn);
    }

    /**
     * The work's Commons categories, however Wikidata names them: the category property, the item's own Commons
     * link, and the Commons link of its "topic's main category" item. Many works have only one of the three.
     */
    private List<String> rootCategories(JsonNode entity) {
        Set<String> roots = new LinkedHashSet<>();
        values(entity.path("claims"), "P373").forEach(name -> roots.add("Category:" + name));
        String own = entity.path("sitelinks").path("commonswiki").path("title").asString("");
        if (own.startsWith("Category:")) {
            roots.add(own);
        }
        for (String categoryItem : items(entity.path("claims"), "P910")) {
            String linked = entity(categoryItem)
                    .path("sitelinks")
                    .path("commonswiki")
                    .path("title")
                    .asString("");
            if (linked.startsWith("Category:")) {
                roots.add(linked);
            }
        }
        return List.copyOf(roots);
    }

    /** The work's articles in the encyclopedias worth asking, by language, at most {@link #ARTICLES} of them. */
    private Map<String, String> articles(JsonNode entity) {
        Map<String, String> articles = new LinkedHashMap<>();
        for (String language : ARTICLE_LANGUAGES) {
            String title = entity.path("sitelinks")
                    .path(language + "wiki")
                    .path("title")
                    .asString("");
            if (!title.isBlank() && articles.size() < ARTICLES) {
                articles.put(language, title);
            }
        }
        return articles;
    }

    private List<String> articleImages(String language, String article) {
        JsonNode pages = get(
                        "https://" + language + ".wikipedia.org/w/api.php",
                        "action",
                        "query",
                        "prop",
                        "images",
                        "imlimit",
                        "50",
                        "titles",
                        article)
                .path("query")
                .path("pages");
        List<String> files = new ArrayList<>();
        for (JsonNode page : pages) {
            page.path("images").forEach(image -> {
                // each encyclopedia names the file namespace in its own language; Commons knows it as File:
                String title = image.path("title").asString("");
                int colon = title.indexOf(':');
                if (colon > 0 && CoverSearch.raster(title)) {
                    files.add("File:" + title.substring(colon + 1));
                }
            });
        }
        return files;
    }

    /**
     * The Wikidata item of a written work by its title, when the catalogue has none: the first hit whose
     * description names the author. A title alone is shared by films, operas and other books.
     */
    public Optional<String> findWork(String title, String author) {
        String lastName = lastName(author);
        if (title == null || title.isBlank() || lastName.isBlank()) {
            return Optional.empty();
        }
        JsonNode hits = get(
                        WIKIDATA,
                        "action",
                        "wbsearchentities",
                        "search",
                        title,
                        "language",
                        "en",
                        "uselang",
                        "en",
                        "type",
                        "item",
                        "limit",
                        "10")
                .path("search");
        for (JsonNode hit : hits) {
            String description = hit.path("description").asString("").toLowerCase(Locale.ROOT);
            if (description.contains(lastName.toLowerCase(Locale.ROOT))
                    && !FILM_OR_STAGE.matcher(description).find()) {
                return Optional.of(hit.path("id").asString());
            }
        }
        return Optional.empty();
    }

    /**
     * The year the credited artist died, when it can be known: from the credit itself ("Charles Livingston Bull
     * (1874–1932)") or from the one person Wikidata knows by exactly that name. Empty for a credit that is not a
     * person's name, and for a name two people share.
     */
    public Optional<Integer> deathYear(String artistCredit) {
        if (artistCredit == null || artistCredit.isBlank()) {
            return Optional.empty();
        }
        return deathYears.computeIfAbsent(artistCredit, this::lookUpDeathYear);
    }

    private Optional<Integer> lookUpDeathYear(String artistCredit) {
        Matcher lifespan = LIFESPAN.matcher(artistCredit);
        if (lifespan.find()) {
            return Optional.of(Integer.parseInt(lifespan.group(2)));
        }
        String name = artistCredit
                .replaceAll("\\(.*?\\)", " ")
                .replaceAll("\\s+", " ")
                .trim();
        if (name.isBlank() || name.length() > 60 || NOT_A_PERSON.matcher(name).find()) {
            return Optional.empty();
        }
        JsonNode hits = get(
                        WIKIDATA,
                        "action",
                        "wbsearchentities",
                        "search",
                        name,
                        "language",
                        "en",
                        "type",
                        "item",
                        "limit",
                        "5")
                .path("search");
        List<Integer> years = new ArrayList<>();
        for (JsonNode hit : hits) {
            if (!name.equalsIgnoreCase(hit.path("label").asString(""))) {
                continue;
            }
            JsonNode claims = entity(hit.path("id").asString()).path("claims");
            if (!items(claims, "P31").contains("Q5")) {
                continue;
            }
            JsonNode time = claims.path("P570")
                    .path(0)
                    .path("mainsnak")
                    .path("datavalue")
                    .path("value")
                    .path("time");
            Matcher year = YEAR_OF.matcher(time.asString(""));
            // a person with no death date on record may be alive: the name then proves nothing
            years.add(year.find() ? Integer.parseInt(year.group(1)) : Integer.MAX_VALUE);
        }
        return years.size() == 1 && years.getFirst() != Integer.MAX_VALUE
                ? Optional.of(years.getFirst())
                : Optional.empty();
    }

    private JsonNode entity(String id) {
        return get(WIKIDATA, "action", "wbgetentities", "ids", id, "props", "claims|sitelinks")
                .path("entities")
                .path(id);
    }

    private static String lastName(String author) {
        String trimmed = author == null ? "" : author.trim();
        return trimmed.isEmpty() ? "" : trimmed.substring(trimmed.lastIndexOf(' ') + 1);
    }

    /** Files Commons' own search finds for these words: a last resort for a work whose category holds too little. */
    public List<CommonsFile> search(String words) {
        return search(words, SEARCH);
    }

    /** The same search, its finds labelled with where they came from. */
    public List<CommonsFile> search(String words, String label) {
        JsonNode hits = get(
                        COMMONS,
                        "action",
                        "query",
                        "list",
                        "search",
                        "srnamespace",
                        "6",
                        "srlimit",
                        String.valueOf(SEARCH_HITS),
                        "srsearch",
                        words)
                .path("query")
                .path("search");
        Map<String, String> foundIn = new LinkedHashMap<>();
        hits.forEach(hit -> {
            String title = hit.path("title").asString("");
            if (CoverSearch.raster(title)) {
                foundIn.putIfAbsent(title, label);
            }
        });
        return describeAll(foundIn);
    }

    private List<CommonsFile> describeAll(Map<String, String> foundIn) {
        List<String> all = List.copyOf(foundIn.keySet());
        List<CommonsFile> files = new ArrayList<>();
        for (int from = 0; from < all.size(); from += BATCH) {
            files.addAll(describe(all.subList(from, Math.min(all.size(), from + BATCH)), foundIn));
        }
        return files;
    }

    public byte[] download(String imageUrl) {
        return restTemplate
                .exchange(URI.create(imageUrl), HttpMethod.GET, new HttpEntity<>(headers()), byte[].class)
                .getBody();
    }

    private List<String> members(String category, String type, int limit) {
        JsonNode members = get(
                        COMMONS,
                        "action",
                        "query",
                        "list",
                        "categorymembers",
                        "cmtitle",
                        category,
                        "cmtype",
                        type,
                        "cmlimit",
                        String.valueOf(limit))
                .path("query")
                .path("categorymembers");
        List<String> titles = new ArrayList<>();
        members.forEach(member -> titles.add(member.path("title").asString()));
        return titles;
    }

    private List<CommonsFile> describe(List<String> titles, Map<String, String> foundIn) {
        JsonNode pages = get(
                        COMMONS,
                        "action",
                        "query",
                        "titles",
                        String.join("|", titles),
                        "prop",
                        "imageinfo|categories",
                        "iiprop",
                        "url|size|mime|extmetadata",
                        "iiurlwidth",
                        String.valueOf(WIDTH),
                        "clshow",
                        "hidden",
                        "cllimit",
                        "max")
                .path("query")
                .path("pages");
        // Commons answers in its own order and under its own spelling of each title; ours is put back
        Map<String, CommonsFile> described = new HashMap<>();
        for (JsonNode page : pages) {
            JsonNode info = page.path("imageinfo").path(0);
            if (info.isMissingNode()) {
                continue;
            }
            JsonNode meta = info.path("extmetadata");
            List<String> categories = new ArrayList<>();
            page.path("categories")
                    .forEach(category ->
                            categories.add(category.path("title").asString().replaceFirst("^Category:", "")));
            String title = page.path("title").asString();
            String thumb = info.path("thumburl").asString("");
            described.put(
                    key(title),
                    new CommonsFile(
                            title,
                            info.path("descriptionurl").asString(""),
                            thumb.isBlank() ? info.path("url").asString("") : thumb,
                            info.path("mime").asString(""),
                            info.path("width").asInt(0),
                            info.path("height").asInt(0),
                            plain(meta.path("Artist").path("value").asString("")),
                            plain(meta.path("LicenseShortName").path("value").asString("")),
                            !"False"
                                    .equalsIgnoreCase(meta.path("Copyrighted")
                                            .path("value")
                                            .asString("")),
                            categories,
                            ""));
        }
        List<CommonsFile> files = new ArrayList<>();
        for (String title : titles) {
            CommonsFile file = described.get(key(title));
            if (file != null) {
                files.add(new CommonsFile(
                        file.title(),
                        file.pageUrl(),
                        file.imageUrl(),
                        file.mime(),
                        file.width(),
                        file.height(),
                        file.artist(),
                        file.licence(),
                        file.copyrighted(),
                        file.hiddenCategories(),
                        foundIn.getOrDefault(title, "")));
            }
        }
        return files;
    }

    /** The items a property points at, by id. */
    private static List<String> items(JsonNode claims, String property) {
        List<String> ids = new ArrayList<>();
        claims.path(property).forEach(claim -> {
            String id = claim.path("mainsnak")
                    .path("datavalue")
                    .path("value")
                    .path("id")
                    .asString("");
            if (!id.isBlank()) {
                ids.add(id);
            }
        });
        return ids;
    }

    private static List<String> values(JsonNode claims, String property) {
        List<String> values = new ArrayList<>();
        claims.path(property).forEach(claim -> {
            JsonNode value = claim.path("mainsnak").path("datavalue").path("value");
            if (value.isString()) {
                values.add(value.asString());
            }
        });
        return values;
    }

    /** Commons writes a title's underscores as spaces; both spellings name one file. */
    private static String key(String title) {
        return title.replace('_', ' ');
    }

    /** The metadata is HTML; a credit line is text. */
    private static String plain(String html) {
        String text = html.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
        return text.length() > 250 ? text.substring(0, 250) : text;
    }

    private JsonNode get(String base, String... parameters) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base).queryParam("format", "json");
        for (int index = 0; index < parameters.length; index += 2) {
            builder.queryParam(parameters[index], "{" + parameters[index] + "}");
        }
        Map<String, String> variables = new HashMap<>();
        for (int index = 0; index < parameters.length; index += 2) {
            variables.put(parameters[index], parameters[index + 1]);
        }
        URI uri = builder.encode().buildAndExpand(variables).toUri();
        JsonNode body = restTemplate
                .exchange(uri, HttpMethod.GET, new HttpEntity<>(headers()), JsonNode.class)
                .getBody();
        return body == null ? tools.jackson.databind.node.MissingNode.getInstance() : body;
    }

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, properties.getUserAgent());
        return headers;
    }
}
