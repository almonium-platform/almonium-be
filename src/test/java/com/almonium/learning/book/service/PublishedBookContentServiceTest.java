package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.almonium.learning.book.dto.response.BookChapter;
import com.almonium.learning.book.dto.response.ChapterVocabulary;
import com.almonium.learning.book.model.entity.Book;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PublishedBookContentServiceTest {
    @Test
    void readsTypedChapterVocabularyWithoutInventingMissingEntries() {
        var response = new ChapterVocabulary(
                UUID.randomUUID(),
                11,
                "en",
                "ready",
                "book-useful-words-in-chapter",
                List.of(new ChapterVocabulary.Word(
                        "lantern", "lanterns", "Two lanterns burned.", "c11.p1", 4, 12, "uncommon")),
                new ChapterVocabulary.Provenance("hash", "lexical-v3", "en_core_web_sm", "3.8.0"));
        Book book = new Book();
        book.setEditionSlug("original");
        when(restTemplate.getForObject(
                        "https://books.example/api/v1/public/editions/{slug}/chapters/{sequence}/vocabulary/",
                        ChapterVocabulary.class,
                        "original",
                        11))
                .thenReturn(response);
        assertThat(service.vocabularyFor(book, 11)).isEqualTo(response);
    }

    @Test
    void missingOrUnpublishedVocabularyIsNotAnEmptySuccessfulDictionary() {
        when(restTemplate.getForObject(anyString(), eq(ChapterVocabulary.class), any(Object[].class)))
                .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not found", null, null, null));
        assertThatThrownBy(() -> service.vocabularyFor(new Book(), 11)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void rejectsVocabularyFromAnotherChapter() {
        when(restTemplate.getForObject(anyString(), eq(ChapterVocabulary.class), any(Object[].class)))
                .thenReturn(new ChapterVocabulary(
                        UUID.randomUUID(), 12, "en", "ready", "book-useful-words-in-chapter", List.of(), null));
        assertThatThrownBy(() -> service.vocabularyFor(new Book(), 11)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void invalidCompanionSpansOrDuplicateIndexesDisableHighlightsOnBothSides() {
        for (String mapping : List.of(
                "{\"primary\":[0],\"secondary\":[9],\"certain\":true}",
                "{\"primary\":[0,0],\"secondary\":[0],\"certain\":true}")) {
            JsonNode payload = objectMapper.readTree("""
                    {"primary_language":"en","secondary_language":"en","blocks":[{
                    "chapter":1,"sequence":1,"block_type":"paragraph",
                    "primary_text":"Rain.","secondary_text":"Light.",
                    "primary_sentences":[{"start":0,"end":5}],
                    "secondary_sentences":[{"start":0,"end":6}],
                    "sentence_alignment":[%s]}]}
                    """.formatted(mapping));
            when(restTemplate.getForObject(anyString(), eq(JsonNode.class), any(Object[].class)))
                    .thenReturn(payload);
            String html = new String(service.parallelTextFor(new Book(), new Book()), StandardCharsets.UTF_8);
            assertThat(html).contains("Rain.", "Light.").doesNotContain("data-alignment");
        }
    }

    @Test
    void invalidOppositeOffsetsDoNotLeaveAnOrphanClickableSentence() {
        JsonNode payload = objectMapper.readTree("""
                {"primary_language":"en","secondary_language":"en","blocks":[{
                "chapter":1,"sequence":1,"block_type":"paragraph",
                "primary_text":"Rain.","secondary_text":"Light.",
                "primary_sentences":[{"start":0,"end":5}],
                "secondary_sentences":[{"start":0,"end":999}],
                "sentence_alignment":[{"primary":[0],"secondary":[0],"certain":true}]}]}
                """);
        when(restTemplate.getForObject(anyString(), eq(JsonNode.class), any(Object[].class)))
                .thenReturn(payload);
        assertThat(new String(service.parallelTextFor(new Book(), new Book()), StandardCharsets.UTF_8))
                .contains("Rain.", "Light.")
                .doesNotContain("data-alignment");
    }

    @Test
    void readsTypedChaptersWithoutInferringAnEditorialLevel() {
        var chapter = new BookChapter(UUID.randomUUID(), 10, "IV", "complete", "B2", List.of("A difficult choice."));
        when(restTemplate.getForObject(anyString(), eq(BookChapter[].class), any(Object[].class)))
                .thenReturn(new BookChapter[] {chapter});
        Book book = new Book();
        book.setEditionSlug("adapted");
        assertThat(service.chaptersFor(book)).containsExactly(chapter);
    }

    @Mock
    RestTemplate restTemplate;

    @Mock
    BookProcessorClient processorClient;

    PublishedBookContentService service;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        service = new PublishedBookContentService(restTemplate, processorClient);
        ReflectionTestUtils.setField(service, "processorUrl", "https://books.example/api/v1");
        objectMapper = new ObjectMapper();
    }

    @Test
    void rendersEscapedBlocksFromProcessorStorage() throws Exception {
        JsonNode[] blocks = objectMapper.readValue("""
                [{"chapter":1,"chapter_title":"One & <Two>","block_type":"heading","text":"One & <Two>"},
                 {"chapter":1,"chapter_title":"One & <Two>","block_type":"paragraph","text":"Stored text."}]
                """, JsonNode[].class);
        when(restTemplate.getForObject(anyString(), eq(JsonNode[].class), any(Object[].class)))
                .thenReturn(blocks);
        Book book = new Book();
        book.setEditionSlug("stored-book-en");

        String html = new String(service.textFor(book), StandardCharsets.UTF_8);

        assertThat(html)
                .isEqualTo(
                        "<section class=\"chapter\"><h2 class=\"chapter-title\" id=\"chapter-1\">One &amp; &lt;Two&gt;</h2>"
                                + "<p>Stored text.</p></section>");
    }

    @Test
    void preservesSameLanguageSidesManyToOneSentencesAndUnicodeOffsets() {
        JsonNode payload = objectMapper.readTree("""
                {"primary_language":"en","secondary_language":"en","blocks":[{
                "chapter":11,"chapter_title":"V","sequence":2,"block_type":"paragraph",
                "primary_text":"😀 Rain. Light.","secondary_text":"Rain and light.",
                "primary_sentences":[{"start":0,"end":7},{"start":8,"end":14}],
                "secondary_sentences":[{"start":0,"end":15}],
                "sentence_alignment":[{"primary":[0,1],"secondary":[0],"certain":true}]}]}
                """);
        when(restTemplate.getForObject(anyString(), eq(JsonNode.class), any(Object[].class)))
                .thenReturn(payload);
        Book primary = new Book();
        primary.setEditionSlug("adapted");
        Book secondary = new Book();
        secondary.setEditionSlug("original");
        String html = new String(service.parallelTextFor(primary, secondary), StandardCharsets.UTF_8);
        assertThat(html).contains("data-side=\"primary\"", "data-side=\"secondary\"", "😀 Rain.", "Light.");
        assertThat(html.split("data-alignment=\"11-2-0\"", -1)).hasSize(4);
    }

    @Test
    void uncertainOrInvalidSentenceSpansFallBackToParagraphText() {
        JsonNode payload = objectMapper.readTree("""
                {"primary_language":"en","secondary_language":"uk","blocks":[{
                "chapter":1,"sequence":1,"block_type":"paragraph",
                "primary_text":"<script>","secondary_text":"Other.",
                "primary_sentences":[{"start":0,"end":999}],
                "secondary_sentences":[{"start":0,"end":6}],
                "sentence_alignment":[{"primary":[0],"secondary":[0],"certain":false}]}]}
                """);
        when(restTemplate.getForObject(anyString(), eq(JsonNode.class), any(Object[].class)))
                .thenReturn(payload);
        String html = new String(service.parallelTextFor(new Book(), new Book()), StandardCharsets.UTF_8);
        assertThat(html).contains("&lt;script&gt;", "Other.").doesNotContain("data-alignment", "<script>");
    }
}
