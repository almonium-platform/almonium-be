package com.almonium.learning.lexicon.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.lexicon.controller.internal.LexiconPublicationController;
import com.almonium.learning.lexicon.dto.response.EntryPublicationResult;
import com.almonium.learning.lexicon.service.EntrySchema;
import com.almonium.learning.lexicon.service.LexiconPublicationService;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class LexiconPublicationControllerTest {
    private static final String SECRET = "test-publisher-secret";
    private static final String VALID_ENTRY = """
            {"language":"en","headword":"bloke","lexemes":[{"lemma":"bloke","pos":"NOUN","kind":"word",
             "forms":[{"form":"bloke","features":{"Number":"Sing"}}],
             "pronunciations":[{"variety":"en-GB","ipa":"/bləʊk/"}],
             "senses":[{"definition":"a man, especially an ordinary one","register":"informal","cefr":"B2",
               "glosses":{"uk":"мужик"},"example":"He seems like a nice bloke, always happy to help.",
               "cues":[{"kind":"wording","text":"a casual British word for a man"},
                       {"kind":"cloze","text":"He seems like a nice ___."}]}]}]}
            """;

    @Mock
    LexiconPublicationService publicationService;

    LexiconPublicationController controller;

    @BeforeEach
    void setUp() {
        controller = new LexiconPublicationController(
                publicationService, new EntrySchema(), new BooksSignature(SECRET), new ObjectMapper());
    }

    @Test
    void refusesABadSignatureBeforeReadingTheBody() {
        byte[] body = publication(VALID_ENTRY).getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        assertThatThrownBy(() -> controller.publish(timestamp, "00", body))
                .isInstanceOfSatisfying(
                        ResponseStatusException.class,
                        exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED));
        verify(publicationService, never()).publish(any());
    }

    @Test
    void answers400WithTheSchemaMessageForAnEntryThatDoesNotPass() {
        String entry = VALID_ENTRY
                .replace("\"pos\":\"NOUN\"", "\"pos\":\"SUBSTANTIVE\"")
                .replace("\"headword\":\"bloke\",", "");
        byte[] body = publication(entry).getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        ResponseEntity<?> response = controller.publish(timestamp, sign(timestamp, body), body);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        EntryPublicationResult result = (EntryPublicationResult) response.getBody();
        assertThat(result.accepted()).isFalse();
        assertThat(result.language()).isEqualTo("en");
        assertThat(result.message()).contains("headword").contains("pos");
        verify(publicationService, never()).publish(any());
    }

    @Test
    void requiresAWriterAndAnEntryObject() {
        byte[] noWriter = ("{\"entry\":" + VALID_ENTRY + "}").getBytes(StandardCharsets.UTF_8);
        byte[] noEntry = "{\"writer\":\"cc-claude-opus-5-5\"}".getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        ResponseEntity<?> writerless = controller.publish(timestamp, sign(timestamp, noWriter), noWriter);
        ResponseEntity<?> entryless = controller.publish(timestamp, sign(timestamp, noEntry), noEntry);

        assertThat(writerless.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(((EntryPublicationResult) writerless.getBody()).message()).contains("writer");
        assertThat(entryless.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(((EntryPublicationResult) entryless.getBody()).message()).contains("entry");
        verify(publicationService, never()).publish(any());
    }

    @Test
    void aBatchReportsEachEntryAndOneBadEntryCostsOnlyItself() {
        when(publicationService.publish(any())).thenReturn(EntryPublicationResult.accepted("en", "bloke", 1));
        String broken = VALID_ENTRY.replace("\"cefr\":\"B2\"", "\"cefr\":\"Z9\"");
        byte[] body =
                ("[" + publication(VALID_ENTRY) + "," + publication(broken) + "]").getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        ResponseEntity<?> response = controller.publish(timestamp, sign(timestamp, body), body);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        @SuppressWarnings("unchecked")
        List<EntryPublicationResult> results = (List<EntryPublicationResult>) response.getBody();
        assertThat(results).hasSize(2);
        assertThat(results.get(0).accepted()).isTrue();
        assertThat(results.get(1).accepted()).isFalse();
        assertThat(results.get(1).message()).contains("cefr");
    }

    private static String publication(String entry) {
        return "{\"writer\":\"cc-claude-opus-5-5\",\"entry\":" + entry
                + ",\"meta\":{\"model\":\"claude-opus-5-5\",\"prompt_version\":\"v1\"},"
                + "\"built_at\":\"2026-10-12T10:00:00Z\"}";
    }

    private static long now() {
        return System.currentTimeMillis() / 1000;
    }

    private static String sign(long timestamp, byte[] body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update((timestamp + ".").getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(mac.doFinal(body));
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}
