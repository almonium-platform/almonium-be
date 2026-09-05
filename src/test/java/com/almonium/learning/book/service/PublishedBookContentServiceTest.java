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
}
