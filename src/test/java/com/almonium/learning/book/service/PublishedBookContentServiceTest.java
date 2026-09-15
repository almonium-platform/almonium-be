package com.almonium.learning.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.almonium.learning.book.model.entity.Book;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PublishedBookContentServiceTest {
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
