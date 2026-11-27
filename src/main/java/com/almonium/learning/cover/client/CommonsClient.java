package com.almonium.learning.cover.client;

import com.almonium.config.properties.CoverProperties;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

/**
 * What Wikimedia holds for a work: the image Wikidata gives it, and the files of its Commons category and that
 * category's subcategories, each with the licence facts its page carries. Reading only; nothing here judges.
 */
@Component
@RequiredArgsConstructor
public class CommonsClient {
    private static final String WIKIDATA = "https://www.wikidata.org/w/api.php";
    private static final String COMMONS = "https://commons.wikimedia.org/w/api.php";
    /** A width Commons serves ready-made: enough for a cover on a dense screen, light enough for a shelf of them. */
    private static final int WIDTH = 500;

    private static final int CATEGORY_FILES = 200;
    private static final int SUBCATEGORIES = 12;
    private static final int SUBCATEGORY_FILES = 50;
    private static final int MAX_FILES = 300;
    private static final int BATCH = 20;

    private final RestTemplate restTemplate;
    private final CoverProperties properties;

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
            List<String> hiddenCategories) {}

    /** The files a work's Wikidata item leads to, its own image first. */
    public List<CommonsFile> filesFor(String wikidataId) {
        JsonNode claims = get(WIKIDATA, "action", "wbgetentities", "ids", wikidataId, "props", "claims")
                .path("entities")
                .path(wikidataId)
                .path("claims");
        Set<String> titles = new LinkedHashSet<>();
        values(claims, "P18").forEach(name -> titles.add("File:" + name));
        for (String category : values(claims, "P373")) {
            titles.addAll(members("Category:" + category, "file", CATEGORY_FILES));
            for (String subcategory : members("Category:" + category, "subcat", SUBCATEGORIES)) {
                titles.addAll(members(subcategory, "file", SUBCATEGORY_FILES));
            }
        }
        List<String> all = titles.stream().limit(MAX_FILES).toList();
        List<CommonsFile> files = new ArrayList<>();
        for (int from = 0; from < all.size(); from += BATCH) {
            files.addAll(describe(all.subList(from, Math.min(all.size(), from + BATCH))));
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

    private List<CommonsFile> describe(List<String> titles) {
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
        List<CommonsFile> files = new ArrayList<>();
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
            String thumb = info.path("thumburl").asString("");
            files.add(new CommonsFile(
                    page.path("title").asString(),
                    info.path("descriptionurl").asString(""),
                    thumb.isBlank() ? info.path("url").asString("") : thumb,
                    info.path("mime").asString(""),
                    info.path("width").asInt(0),
                    info.path("height").asInt(0),
                    plain(meta.path("Artist").path("value").asString("")),
                    plain(meta.path("LicenseShortName").path("value").asString("")),
                    !"False"
                            .equalsIgnoreCase(
                                    meta.path("Copyrighted").path("value").asString("")),
                    categories));
        }
        return files;
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
        java.util.Map<String, String> variables = new java.util.HashMap<>();
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
