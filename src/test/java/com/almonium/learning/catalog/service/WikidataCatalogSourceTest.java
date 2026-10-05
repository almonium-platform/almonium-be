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
        if (query.contains("AS ?p)")) {
            rows = "?w\t?l\t?lang\t?p\t?i\n" + E + "Q25338>\t151\t\"fr\"\t1943\t1942\n";
        } else if (query.contains("SELECT DISTINCT ?w ?a")) {
            rows = "?w\t?a\n" + E + "Q25338>\t" + E + "Q2908>\n";
        } else if (query.contains("?france")) {
            rows = "?a\t?nameEn\t?nameMul\t?nameAny\t?b\t?d\t?france\n" + E
                    + "Q2908>\t\"Antoine de Saint-Exupéry\"@en\t\t\t1900\t1944\t1\n";
        } else if (query.contains("?manner")) {
            rows = "?a\n" + E + "Q2908>\n";
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
        assertThat(snapshot.works().get("Q25338").languageCode()).isEqualTo("fr");
        assertThat(snapshot.authorsByWork().get("Q25338")).containsExactly("Q2908");
        assertThat(snapshot.people().get("Q2908").diedForFrance()).isTrue();
        assertThat(snapshot.people().get("Q2908").deathYear()).isEqualTo(1944);
        assertThat(snapshot.people().get("Q2908").repressed()).isTrue();
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
        assertThat(requests.get()).isEqualTo(7 + 2);
    }

    @Test
    void anEndpointThatKeepsRefusingFailsTheImport() {
        refusals.addAll(List.of(429, 429, 429));

        assertThatThrownBy(() -> source().fetch()).hasMessageContaining("429");
    }
}
