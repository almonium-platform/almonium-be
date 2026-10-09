package com.almonium.learning.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Against a stand-in SPARQL endpoint: what one import reads, and how a busy endpoint is waited out. */
class WikidataCatalogSourceTest {
    HttpServer server;
    final Deque<Integer> refusals = new ArrayDeque<>();
    final AtomicInteger requests = new AtomicInteger();

    static final String E = "<http://www.wikidata.org/entity/";

    @BeforeEach
    void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/sparql", exchange -> {
            requests.incrementAndGet();
            String query = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Integer refusal = refusals.poll();
            byte[] body =
                    refusal != null ? new byte[0] : answer(java.net.URLDecoder.decode(query, StandardCharsets.UTF_8));
            exchange.sendResponseHeaders(refusal != null ? refusal : 200, body.length == 0 ? -1 : body.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(body);
            }
        });
        server.start();
    }

    @AfterEach
    void stop() {
        server.stop(0);
    }

    /** One row per query shape, told apart by what each query selects. */
    static byte[] answer(String query) {
        String rows;
        if (query.contains("?france") && query.contains("P655")) {
            rows = "?a\t?nameEn\t?nameMul\t?nameAny\t?b\t?d\t?france\n" + E
                    + "Q99>\t\"Katherine Woods\"@en\t\t\t1886\t1968\t0\n";
        } else if (query.contains("SELECT DISTINCT ?e ?a")) {
            rows = "?e\t?a\n" + E + "Q1>\t" + E + "Q99>\n";
        } else if (query.contains("SELECT DISTINCT ?w ?e ?article")) {
            rows = "?w\t?e\t?article\n" + E + "Q25338>\t" + E
                    + "Q1>\t<https://en.wikisource.org/wiki/The_Little_Prince>\n";
        } else if (query.contains("SELECT DISTINCT ?w ?article")) {
            rows = "?w\t?article\n" + E + "Q25338>\t<https://fr.wikisource.org/wiki/Le_Petit_Prince>\n" + E
                    + "Q25338>\t<https://wikisource.org/wiki/Multilingual_page>\n";
        } else if (query.contains("P103")) {
            rows = "?w\t?lang\n" + E + "Q25338>\t\"en\"\n";
        } else if (query.contains("SELECT DISTINCT ?w WHERE { VALUES ?cls")) {
            rows = "?w\n" + E + "Q25338>\n";
        } else if (query.contains("AS ?p)")) {
            rows = "?w\t?l\t?lang\t?p\t?i\n" + E + "Q25338>\t151\t\"fr\"\t1943\t1942\n";
        } else if (query.contains("SELECT DISTINCT ?w ?a")) {
            rows = "?w\t?a\n" + E + "Q25338>\t" + E + "Q2908>\n";
        } else if (query.contains("?france")) {
            rows = "?a\t?nameEn\t?nameMul\t?nameAny\t?b\t?d\t?france\n" + E
                    + "Q2908>\t\"Antoine de Saint-Exupéry\"@en\t\t\t1900\t1944\t1\n";
        } else if (query.contains("?manner")) {
            rows = "?a\n" + E + "Q2908>\n";
        } else if (query.contains("?publisher")) {
            rows = "?w\t?publisher\n" + E + "Q25338>\t\"Gallimard\"@en\n";
        } else if (query.contains("?gutenberg")) {
            rows = "?w\t?gutenberg\n";
        } else if (query.contains("= ?code")) {
            rows = "?w\t?label\n" + E + "Q25338>\t\"Le Petit Prince\"@fr\n";
        } else {
            rows = "?w\t?label\n" + E + "Q25338>\t\"The Little Prince\"@en\n";
        }
        return rows.getBytes(StandardCharsets.UTF_8);
    }

    WikidataCatalogSource source() {
        return new WikidataCatalogSource(
                "http://127.0.0.1:" + server.getAddress().getPort() + "/sparql",
                List.of("en", "fr"),
                List.of(Duration.ZERO, Duration.ZERO));
    }

    @Test
    void readsWorksAuthorsAndTitlesPreferringThePublicationYear() {
        var snapshot = source().fetch();

        assertThat(snapshot.works().get("Q25338").year()).isEqualTo(1943);
        // The work's own language wins over its author's native one.
        assertThat(snapshot.works().get("Q25338").languageCode()).isEqualTo("fr");
        assertThat(snapshot.works().get("Q25338").literary()).isTrue();
        assertThat(snapshot.authorsByWork().get("Q25338")).containsExactly("Q2908");
        assertThat(snapshot.people().get("Q2908").diedForFrance()).isTrue();
        assertThat(snapshot.people().get("Q2908").deathYear()).isEqualTo(1944);
        assertThat(snapshot.people().get("Q2908").repressed()).isTrue();
        assertThat(snapshot.publisherByWork()).containsEntry("Q25338", "Gallimard");
        assertThat(snapshot.wikisourceByWork().get("Q25338"))
                .containsExactlyInAnyOrder(
                        new WikidataCatalogSource.WikisourcePage(
                                "https://fr.wikisource.org/wiki/Le_Petit_Prince", "fr", null, List.of()),
                        new WikidataCatalogSource.WikisourcePage(
                                "https://en.wikisource.org/wiki/The_Little_Prince", "en", "Q1", List.of("Q99")));
        assertThat(snapshot.people().get("Q99").deathYear()).isEqualTo(1968);
        assertThat(snapshot.labelsByWork().get("Q25338"))
                .extracting(WikidataCatalogSource.Label::text)
                .containsExactlyInAnyOrder("The Little Prince", "Le Petit Prince");
    }

    @Test
    void aBusyEndpointIsAskedAgainAfterAPause() {
        refusals.add(429);
        refusals.add(503);

        var snapshot = source().fetch();

        assertThat(snapshot.works()).containsKey("Q25338");
        assertThat(requests.get()).isEqualTo(14 + 2);
    }

    @Test
    void anEndpointThatKeepsRefusingFailsTheImport() {
        refusals.addAll(List.of(429, 429, 429));

        assertThatThrownBy(() -> source().fetch()).hasMessageContaining("429");
    }
}
