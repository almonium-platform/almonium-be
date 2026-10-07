package com.almonium.learning.catalog.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

/**
 * Notable literary works from Wikidata, through a SPARQL endpoint (QLever by default: the public Wikidata query
 * service times out on queries this size). A work is notable when at least one Wikipedia has an article on it. Five
 * flat queries instead of one joined one, so no query multiplies rows.
 */
@Slf4j
@Component
public class WikidataCatalogSource {
    /** Literary work, novel, novella, short story, poem, play, poetry and story collections, fairy tale, epic, book. */
    private static final String WORK_CLASSES = "wd:Q7725634 wd:Q8261 wd:Q149537 wd:Q49084 wd:Q5185279 wd:Q25379 "
            + "wd:Q1279564 wd:Q1371849 wd:Q699 wd:Q37484 wd:Q571 wd:Q47461344";

    /** "Mort pour la France", as an award received (P166). */
    private static final String DIED_FOR_FRANCE = "wd:Q3324507";

    private static final String PREFIXES = """
            PREFIX wdt: <http://www.wikidata.org/prop/direct/>
            PREFIX wd: <http://www.wikidata.org/entity/>
            PREFIX wikibase: <http://wikiba.se/ontology#>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            """;

    // QLever drops every row when wikibase:sitelinks is compared directly; adding zero first works.
    private static final String NOTABLE_WORK = "VALUES ?cls { " + WORK_CLASSES + " } ?w wdt:P31 ?cls . "
            + "?w wikibase:sitelinks ?links . BIND(?links + 0 AS ?linkCount) FILTER(?linkCount > 0) ";

    /** How long to wait before each retry of a refused query. */
    static final List<Duration> RETRY_PAUSES =
            List.of(Duration.ofSeconds(30), Duration.ofMinutes(1), Duration.ofMinutes(2));

    private final List<Duration> retryPauses;
    private final RestClient restClient;
    private final String endpoint;
    private final List<String> titleLanguages;

    @Autowired
    public WikidataCatalogSource(
            @Value("${app.catalog.sparql-url:https://qlever.dev/api/wikidata}") String endpoint,
            @Value("${app.catalog.title-languages:en,mul,de,uk,ru,fr,es,it,pl,pt,nl,cs,sv}")
                    List<String> titleLanguages) {
        this(endpoint, titleLanguages, RETRY_PAUSES);
    }

    WikidataCatalogSource(String endpoint, List<String> titleLanguages, List<Duration> retryPauses) {
        this.retryPauses = List.copyOf(retryPauses);
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofMinutes(10));
        this.restClient = RestClient.builder().requestFactory(factory).build();
        this.endpoint = endpoint;
        this.titleLanguages = titleLanguages;
    }

    public record Work(String id, int popularity, String languageCode, Integer year) {}

    /** {@code repressed}: a Soviet author who died a violent death in 1930–1953, the years of the purges. */
    public record Person(
            String id, String name, Integer birthYear, Integer deathYear, boolean diedForFrance, boolean repressed) {
        public Person(String id, String name, Integer birthYear, Integer deathYear, boolean diedForFrance) {
            this(id, name, birthYear, deathYear, diedForFrance, false);
        }
    }

    public record Label(String workId, String text, String language) {}

    /** Everything one import needs, keyed by Q-id. */
    public record Snapshot(
            Map<String, Work> works,
            Map<String, List<String>> authorsByWork,
            Map<String, Person> people,
            Map<String, List<Label>> labelsByWork,
            Map<String, Integer> gutenbergByWork,
            Map<String, String> publisherByWork) {

        public Snapshot(
                Map<String, Work> works,
                Map<String, List<String>> authorsByWork,
                Map<String, Person> people,
                Map<String, List<Label>> labelsByWork,
                Map<String, Integer> gutenbergByWork) {
            this(works, authorsByWork, people, labelsByWork, gutenbergByWork, Map.of());
        }
    }

    public Snapshot fetch() {
        Map<String, Work> works = new HashMap<>();
        query(
                "SELECT ?w (SAMPLE(?links) AS ?l) (SAMPLE(?code) AS ?lang) (MIN(YEAR(?published)) AS ?p) "
                        + "(MIN(YEAR(?made)) AS ?i) WHERE { "
                        + NOTABLE_WORK
                        + "OPTIONAL { ?w wdt:P407 ?language . ?language wdt:P424 ?code } "
                        + "OPTIONAL { ?w wdt:P577 ?published } OPTIONAL { ?w wdt:P571 ?made } } GROUP BY ?w",
                cells -> {
                    Integer links = SparqlTsv.integer(SparqlTsv.value(cells.get(1)));
                    // First publication when recorded; the date of writing ("inception") only without one, since
                    // an earlier year makes the United States' publication-based term look shorter than it is.
                    Integer published = SparqlTsv.integer(SparqlTsv.value(cells.get(3)));
                    Integer written = SparqlTsv.integer(SparqlTsv.value(cells.get(4)));
                    works.put(
                            SparqlTsv.value(cells.get(0)),
                            new Work(
                                    SparqlTsv.value(cells.get(0)),
                                    links == null ? 0 : links,
                                    emptyToNull(SparqlTsv.value(cells.get(2))),
                                    published != null ? published : written));
                });

        Map<String, List<String>> authorsByWork = new HashMap<>();
        query(
                "SELECT DISTINCT ?w ?a WHERE { " + NOTABLE_WORK + "?w wdt:P50 ?a }",
                cells -> authorsByWork
                        .computeIfAbsent(SparqlTsv.value(cells.get(0)), key -> new ArrayList<>())
                        .add(SparqlTsv.value(cells.get(1))));

        Map<String, Person> people = new HashMap<>();
        query(
                "SELECT ?a (SAMPLE(?en) AS ?nameEn) (SAMPLE(?mul) AS ?nameMul) (SAMPLE(?any) AS ?nameAny) "
                        + "(MIN(YEAR(?born)) AS ?b) (MAX(YEAR(?died)) AS ?d) (COUNT(?award) AS ?france) WHERE { "
                        + "{ SELECT DISTINCT ?a WHERE { " + NOTABLE_WORK + "?w wdt:P50 ?a } } "
                        + "OPTIONAL { ?a rdfs:label ?en FILTER(LANG(?en) = \"en\") } "
                        + "OPTIONAL { ?a rdfs:label ?mul FILTER(LANG(?mul) = \"mul\") } "
                        + "OPTIONAL { ?a rdfs:label ?any } "
                        + "OPTIONAL { ?a wdt:P569 ?born } OPTIONAL { ?a wdt:P570 ?died } "
                        + "OPTIONAL { ?a wdt:P166 ?award FILTER(?award = " + DIED_FOR_FRANCE + ") } } GROUP BY ?a",
                cells -> {
                    String name = firstNonEmpty(
                            SparqlTsv.value(cells.get(1)),
                            SparqlTsv.value(cells.get(2)),
                            SparqlTsv.value(cells.get(3)));
                    Integer france = SparqlTsv.integer(SparqlTsv.value(cells.get(6)));
                    people.put(
                            SparqlTsv.value(cells.get(0)),
                            new Person(
                                    SparqlTsv.value(cells.get(0)),
                                    name,
                                    SparqlTsv.integer(SparqlTsv.value(cells.get(4))),
                                    SparqlTsv.integer(SparqlTsv.value(cells.get(5))),
                                    france != null && france > 0));
                });

        // Soviet citizens (USSR, Ukrainian SSR, Russian SFSR) whose manner of death was capital punishment, homicide or
        // "unnatural death", dying in 1930–1953: the purges' victims, many rehabilitated after death.
        query(
                "SELECT DISTINCT ?a WHERE { { SELECT DISTINCT ?a WHERE { " + NOTABLE_WORK + "?w wdt:P50 ?a } } "
                        + "?a wdt:P570 ?died . BIND(YEAR(?died) AS ?y) FILTER(?y >= 1930 && ?y <= 1953) "
                        + "?a wdt:P27 ?country . FILTER(?country IN (wd:Q15180, wd:Q133356, wd:Q2184)) "
                        + "?a wdt:P1196 ?manner . FILTER(?manner IN (wd:Q8454, wd:Q855919, wd:Q149086)) }",
                cells -> people.computeIfPresent(
                        SparqlTsv.value(cells.get(0)),
                        (id, person) -> new Person(
                                person.id(),
                                person.name(),
                                person.birthYear(),
                                person.deathYear(),
                                person.diedForFrance(),
                                true)));

        Map<String, List<Label>> labelsByWork = new HashMap<>();
        Consumer<List<String>> label = raw -> {
            String workId = SparqlTsv.value(raw.get(0));
            labelsByWork
                    .computeIfAbsent(workId, key -> new ArrayList<>())
                    .add(new Label(workId, SparqlTsv.value(raw.get(1)), SparqlTsv.language(raw.get(1))));
        };
        String languages = String.join(
                ", ", titleLanguages.stream().map(code -> "\"" + code + "\"").toList());
        query(
                "SELECT DISTINCT ?w ?label WHERE { " + NOTABLE_WORK + "?w rdfs:label ?label "
                        + "FILTER(LANG(?label) IN (" + languages + ")) }",
                label);
        // The title in the work's own language, whatever that language is.
        query(
                "SELECT DISTINCT ?w ?label WHERE { " + NOTABLE_WORK
                        + "?w wdt:P407 ?language . ?language wdt:P424 ?code . ?w rdfs:label ?label "
                        + "FILTER(LANG(?label) = ?code) }",
                label);

        Map<String, Integer> gutenbergByWork = new HashMap<>();
        query(
                "SELECT ?w (MIN(?g) AS ?gutenberg) WHERE { " + NOTABLE_WORK + "?w wdt:P2034 ?id . "
                        + "BIND(xsd:integer(?id) AS ?g) } GROUP BY ?w",
                cells -> {
                    Integer id = SparqlTsv.integer(SparqlTsv.value(cells.get(1)));
                    if (id != null) {
                        gutenbergByWork.put(SparqlTsv.value(cells.get(0)), id);
                    }
                });

        // The publisher, for the /ops demand page's "Rights holder" column: research, not a verdict.
        Map<String, String> publisherByWork = new HashMap<>();
        query(
                "SELECT ?w (SAMPLE(?name) AS ?publisher) WHERE { " + NOTABLE_WORK + "?w wdt:P123 ?p . "
                        + "?p rdfs:label ?name FILTER(LANG(?name) = \"en\") } GROUP BY ?w",
                cells -> {
                    String publisher = SparqlTsv.value(cells.get(1));
                    if (!publisher.isEmpty()) {
                        publisherByWork.put(SparqlTsv.value(cells.get(0)), publisher);
                    }
                });

        log.info(
                "Wikidata catalogue: {} works, {} authors, {} with labels, {} with a Gutenberg id",
                works.size(),
                people.size(),
                labelsByWork.size(),
                gutenbergByWork.size());
        return new Snapshot(works, authorsByWork, people, labelsByWork, gutenbergByWork, publisherByWork);
    }

    /** Streams the result rows (header skipped) as raw cells; the caller decodes them. */
    /**
     * Streams the result rows (header skipped) as raw cells; the caller decodes them. A refusal before any row arrives
     * (QLever answers 429 when busy, or a 5xx) is retried after a pause, three times; a failure mid-stream is not, since
     * rows already handed over would arrive twice.
     */
    private void query(String select, Consumer<List<String>> row) {
        for (int attempt = 1; ; attempt++) {
            try {
                queryOnce(select, row);
                return;
            } catch (RefusedException refused) {
                if (attempt > retryPauses.size()) {
                    throw new IllegalStateException("SPARQL endpoint answered " + refused.getMessage(), refused);
                }
                Duration pause = retryPauses.get(attempt - 1);
                log.warn("SPARQL endpoint answered {}; retrying in {} s", refused.getMessage(), pause.toSeconds());
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted while waiting to retry", interrupted);
                }
            }
        }
    }

    private void queryOnce(String select, Consumer<List<String>> row) {
        LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("query", PREFIXES + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" + select);
        long started = System.nanoTime();
        int[] count = {0};
        restClient
                .post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.valueOf("text/tab-separated-values"))
                .body(form)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isError()) {
                        throw new RefusedException(response.getStatusCode().toString());
                    }
                    try (BufferedReader reader =
                            new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                        reader.readLine();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.isEmpty()) {
                                row.accept(SparqlTsv.raw(line));
                                count[0]++;
                            }
                        }
                    }
                    return null;
                });
        log.info("SPARQL query returned {} rows in {} ms", count[0], (System.nanoTime() - started) / 1_000_000);
    }

    /** The endpoint refused the query before sending any row. */
    static final class RefusedException extends RuntimeException {
        RefusedException(String status) {
            super(status);
        }
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    private static String firstNonEmpty(String... values) {
        for (String value : values) {
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
    }
}
