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
                processorUrl + "/public/editions/{slug}/blocks/", JsonNode[].class, book.getEditionSlug());
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
            appendBlock(
                    html, block.path("block_type").asText(), block.path("text").asText(), chapter);
        }
        if (chapter >= 0) html.append("</section>");
        return html.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] parallelTextFor(Book primary, Book secondary) {
        JsonNode payload = restTemplate.getForObject(
                processorUrl + "/public/editions/{slug}/parallel/{otherSlug}/",
                JsonNode.class,
                primary.getEditionSlug(),
                secondary.getEditionSlug());
        if (payload == null || !payload.path("blocks").isArray()) {
            throw new IllegalStateException("Published editions have no aligned content");
        }

        StringBuilder html = new StringBuilder();
        int chapter = -1;
        for (JsonNode block : payload.path("blocks")) {
            int currentChapter = block.path("chapter").asInt();
            if (currentChapter != chapter) {
                if (chapter >= 0) html.append("</section>");
                chapter = currentChapter;
                html.append("<section class=\"chapter\">");
            }
            appendParallelBlock(
                    html,
                    block.path("block_type").asText(),
                    payload.path("primary_language").asText(),
                    payload.path("secondary_language").asText(),
                    block.path("primary_text").asText(),
                    block.path("secondary_text").asText(),
                    chapter);
        }
        if (chapter >= 0) html.append("</section>");
        return html.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void appendBlock(StringBuilder html, String type, String text, int chapter) {
        String escapedText = escapedText(text);
        if ("heading".equals(type))
            html.append("<h2 id=\"chapter-")
                    .append(chapter)
                    .append("\">")
                    .append(escapedText)
                    .append("</h2>");
        else if ("blockquote".equals(type))
            html.append("<blockquote>").append(escapedText).append("</blockquote>");
        else if ("separator".equals(type)) html.append("<hr>");
        else if (!"image".equals(type)) html.append("<p>").append(escapedText).append("</p>");
    }

    private String escapedText(String text) {
        return HtmlUtils.htmlEscape(text).replace("\n", "<br>");
    }

    private void appendParallelBlock(
            StringBuilder html,
            String type,
            String primaryLanguage,
            String secondaryLanguage,
            String primaryText,
            String secondaryText,
            int chapter) {
        String segments = "<span class=\"seg-pair\"><span class=\"segment\" lang=\""
                + HtmlUtils.htmlEscape(primaryLanguage)
                + "\">"
                + escapedText(primaryText)
                + "</span><span class=\"segment\" lang=\""
                + HtmlUtils.htmlEscape(secondaryLanguage)
                + "\">"
                + escapedText(secondaryText)
                + "</span></span>";
        if ("heading".equals(type))
            html.append("<h2 id=\"chapter-")
                    .append(chapter)
                    .append("\">")
                    .append(segments)
                    .append("</h2>");
        else if ("blockquote".equals(type))
            html.append("<blockquote>").append(segments).append("</blockquote>");
        else if ("separator".equals(type)) html.append("<hr>");
        else if (!"image".equals(type)) html.append("<p>").append(segments).append("</p>");
    }
}
