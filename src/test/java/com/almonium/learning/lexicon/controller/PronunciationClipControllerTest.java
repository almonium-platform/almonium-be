package com.almonium.learning.lexicon.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.lexicon.controller.internal.PronunciationClipController;
import com.almonium.learning.lexicon.dto.request.ClipSubmission;
import com.almonium.learning.lexicon.dto.response.ClipSubmissionResult;
import com.almonium.learning.lexicon.dto.response.UnvoicedPronunciation;
import com.almonium.learning.lexicon.service.AudioClipService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PronunciationClipControllerTest {
    private static final String SECRET = "test-publisher-secret";
    private static final byte[] NO_BODY = new byte[0];
    private static final String SUBMISSION = """
            {"engine":"google","voice":"Charon","model":"chirp3-hd","pace":1.0,"format":"mp3_44100_128",
             "engine_input":"bonjour","status":"verified","duration_ms":640,"transcript":null,
             "character_cost":7.00,"audio_base64":"//uQZAAA"}
            """;

    @Mock
    AudioClipService clipService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(
                        new PronunciationClipController(clipService, new BooksSignature(SECRET), new ObjectMapper()))
                .build();
    }

    @Test
    void listsThePronunciationsWithoutAClipAndCapsTheLimit() throws Exception {
        UUID id = UUID.randomUUID();
        UUID lexemeId = UUID.randomUUID();
        when(clipService.withoutClips("fr", PronunciationClipController.MAX_LIMIT, 10))
                .thenReturn(List.of(new UnvoicedPronunciation(
                        id, lexemeId, "bonjour", "bonjour", "INTJ", null, null, "fr-FR", 1, "/bɔ̃.ʒuʀ/", null, null)));
        long timestamp = now();

        mvc.perform(get("/internal/lexicon/pronunciations")
                        .param("language", "fr")
                        .param("without", "clip")
                        .param("limit", "5000")
                        .param("offset", "10")
                        .headers(signed(timestamp, NO_BODY)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].lexemeId").value(lexemeId.toString()))
                .andExpect(jsonPath("$[0].headword").value("bonjour"))
                .andExpect(jsonPath("$[0].pos").value("INTJ"))
                .andExpect(jsonPath("$[0].formId").isEmpty())
                .andExpect(jsonPath("$[0].variety").value("fr-FR"))
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[0].phonemic").value("/bɔ̃.ʒuʀ/"))
                .andExpect(jsonPath("$[0].engineInput").isEmpty());
    }

    @Test
    void theListingOnlyKnowsWithoutClipAndTwoLetterLanguages() throws Exception {
        long timestamp = now();

        mvc.perform(get("/internal/lexicon/pronunciations")
                        .param("language", "fr")
                        .param("without", "reading")
                        .headers(signed(timestamp, NO_BODY)))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/internal/lexicon/pronunciations")
                        .param("language", "fra")
                        .param("without", "clip")
                        .headers(signed(timestamp, NO_BODY)))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/internal/lexicon/pronunciations")
                        .param("language", "fr")
                        .headers(signed(timestamp, NO_BODY)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(clipService);
    }

    @Test
    void refusesABadSignatureBeforeTouchingTheService() throws Exception {
        byte[] body = SUBMISSION.getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        mvc.perform(get("/internal/lexicon/pronunciations")
                        .param("language", "fr")
                        .param("without", "clip")
                        .header("X-Almonium-Books-Timestamp", timestamp)
                        .header("X-Almonium-Books-Signature", "00"))
                .andExpect(status().isUnauthorized());
        mvc.perform(put("/internal/lexicon/pronunciations/{id}/clips", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .headers(signed(timestamp, NO_BODY)))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(clipService);
    }

    @Test
    void storesAClipFromItsSnakeCaseBodyAndSaysWhetherItWroteTheObject() throws Exception {
        UUID pronunciationId = UUID.randomUUID();
        UUID clipId = UUID.randomUUID();
        when(clipService.store(eq(pronunciationId), any()))
                .thenReturn(new ClipSubmissionResult(clipId, "audio/fr-FR/Charon/abc.mp3", false));
        byte[] body = SUBMISSION.getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        mvc.perform(put("/internal/lexicon/pronunciations/{id}/clips", pronunciationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .headers(signed(timestamp, body)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clip_id").value(clipId.toString()))
                .andExpect(jsonPath("$.object_key").value("audio/fr-FR/Charon/abc.mp3"))
                .andExpect(jsonPath("$.stored").value(false));

        ArgumentCaptor<ClipSubmission> submission = ArgumentCaptor.forClass(ClipSubmission.class);
        verify(clipService).store(eq(pronunciationId), submission.capture());
        assertThat(submission.getValue().engine()).isEqualTo("google");
        assertThat(submission.getValue().engineInput()).isEqualTo("bonjour");
        assertThat(submission.getValue().pace()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(submission.getValue().durationMs()).isEqualTo(640);
        assertThat(submission.getValue().characterCost()).isEqualByComparingTo("7.00");
        assertThat(submission.getValue().audioBase64()).isEqualTo("//uQZAAA");
    }

    @Test
    void answers400ForABadSubmissionAnd404ForAnUnknownPronunciation() throws Exception {
        UUID unknown = UUID.randomUUID();
        UUID known = UUID.randomUUID();
        when(clipService.store(eq(unknown), any())).thenThrow(new EntityNotFoundException("No pronunciation"));
        when(clipService.store(eq(known), any())).thenThrow(new IllegalArgumentException("audio_base64 is empty"));
        byte[] body = SUBMISSION.getBytes(StandardCharsets.UTF_8);
        byte[] malformed = "{\"engine\":".getBytes(StandardCharsets.UTF_8);
        long timestamp = now();

        mvc.perform(put("/internal/lexicon/pronunciations/{id}/clips", unknown)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .headers(signed(timestamp, body)))
                .andExpect(status().isNotFound());
        mvc.perform(put("/internal/lexicon/pronunciations/{id}/clips", known)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .headers(signed(timestamp, body)))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/internal/lexicon/pronunciations/{id}/clips", known)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformed)
                        .headers(signed(timestamp, malformed)))
                .andExpect(status().isBadRequest());
    }

    private static long now() {
        return System.currentTimeMillis() / 1000;
    }

    private static HttpHeaders signed(long timestamp, byte[] body) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Almonium-Books-Timestamp", Long.toString(timestamp));
        headers.set("X-Almonium-Books-Signature", sign(timestamp, body));
        return headers;
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
