package com.almonium.learning.book.service;

import com.almonium.learning.book.model.entity.Book;
import com.fasterxml.jackson.databind.JsonNode;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

@Service
@RequiredArgsConstructor
public class PublishedBookContentService {
    private final RestTemplate restTemplate;

    @Value("${app.books.processor-url}")
    private String processorUrl;

    public byte[] textFor(Book book) {
        JsonNode[] blocks = restTemplate.getForObject(
                processorUrl + "/public/editions/{slug}/blocks/", JsonNode[].class, book.getProcessorEditionSlug());
        if (blocks == null) {
            throw new IllegalStateException("Published edition has no content");
        }
        StringBuilder html = new StringBuilder();
        int chapter = -1;
        for (JsonNode block : blocks) {
            int currentChapter = block.path("chapter").asInt();
            if (currentChapter != chapter) {
                if (chapter >= 0) html.append("</section>");
                chapter = currentChapter;
                html.append("<section class=\"chapter\">");
            }
            String text = HtmlUtils.htmlEscape(block.path("text").asText()).replace("\n", "<br>");
            String type = block.path("block_type").asText();
            if ("heading".equals(type))
                html.append("<h2 id=\"chapter-")
                        .append(chapter)
                        .append("\">")
                        .append(text)
                        .append("</h2>");
            else if ("blockquote".equals(type))
                html.append("<blockquote>").append(text).append("</blockquote>");
            else if ("separator".equals(type)) html.append("<hr>");
            else if (!"image".equals(type)) html.append("<p>").append(text).append("</p>");
        }
        if (chapter >= 0) html.append("</section>");
        return html.toString().getBytes(StandardCharsets.UTF_8);
    }
}
