package com.almonium.learning.book.service;

import com.almonium.learning.book.model.entity.Book;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;
import tools.jackson.databind.JsonNode;

@Service
@RequiredArgsConstructor
public class PublishedBookContentService {
    private final RestTemplate restTemplate;
    private final BookProcessorClient processorClient;

    @Value("${app.books.processor-url}")
    private String processorUrl;

    public byte[] textFor(Book book) {
        JsonNode[] blocks = restTemplate.getForObject(
                processorUrl + "/public/editions/{slug}/blocks/", JsonNode[].class, book.getEditionSlug());
        if (blocks == null) {
            throw new IllegalStateException("Published edition has no content");
        }
        return renderBlocks(blocks);
    }

    public byte[] privateTextFor(java.util.UUID importId, java.util.UUID ownerId) {
        JsonNode[] blocks = processorClient.privateBlocks(importId, ownerId);
        if (blocks == null) {
            throw new IllegalStateException("Private edition has no content");
        }
        return renderBlocks(blocks);
    }

    private byte[] renderBlocks(JsonNode[] blocks) {
        StringBuilder html = new StringBuilder();
        int chapter = -1;
        for (JsonNode block : blocks) {
            int currentChapter = block.path("chapter").asInt(0);
            String chapterTitle = block.path("chapter_title").asString();
            if (currentChapter != chapter) {
                if (chapter >= 0) html.append("</section>");
                chapter = currentChapter;
                html.append("<section class=\"chapter\">");
                appendChapterHeading(
                        html,
                        chapter,
                        chapterTitle.isBlank()
                                        && "heading"
                                                .equals(block.path("block_type").asString())
                                ? block.path("text").asString()
                                : chapterTitle);
            }
            String blockType = block.path("block_type").asString();
            String blockText = block.path("text").asString();
            if (!("heading".equals(blockType) && blockText.equals(chapterTitle))) {
                appendBlock(html, blockType, blockText);
            }
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
            int currentChapter = block.path("chapter").asInt(0);
            String chapterTitle = block.path("chapter_title").asString();
            if (currentChapter != chapter) {
                if (chapter >= 0) html.append("</section>");
                chapter = currentChapter;
                html.append("<section class=\"chapter\">");
                appendChapterHeading(html, chapter, chapterTitle);
            }
            appendParallelBlock(
                    html,
                    block.path("block_type").asString(),
                    payload.path("primary_language").asString(),
                    payload.path("secondary_language").asString(),
                    block.path("primary_text").asString(),
                    block.path("secondary_text").asString(),
                    chapterTitle);
        }
        if (chapter >= 0) html.append("</section>");
        return html.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void appendChapterHeading(StringBuilder html, int chapter, String title) {
        String displayTitle = title.isBlank() ? "Chapter " + (chapter + 1) : title;
        html.append("<h2 class=\"chapter-title\" id=\"chapter-")
                .append(chapter)
                .append("\">")
                .append(escapedText(displayTitle))
                .append("</h2>");
    }

    private void appendBlock(StringBuilder html, String type, String text) {
        String escapedText = escapedText(text);
        if ("heading".equals(type)) html.append("<h3>").append(escapedText).append("</h3>");
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
            String chapterTitle) {
        String segments = "<span class=\"seg-pair\"><span class=\"segment\" lang=\""
                + HtmlUtils.htmlEscape(primaryLanguage)
                + "\">"
                + escapedText(primaryText)
                + "</span><span class=\"segment\" lang=\""
                + HtmlUtils.htmlEscape(secondaryLanguage)
                + "\">"
                + escapedText(secondaryText)
                + "</span></span>";
        if ("heading".equals(type)) {
            if (!primaryText.equals(chapterTitle)) {
                html.append("<h3>").append(segments).append("</h3>");
            }
        } else if ("blockquote".equals(type))
            html.append("<blockquote>").append(segments).append("</blockquote>");
        else if ("separator".equals(type)) html.append("<hr>");
        else if (!"image".equals(type)) html.append("<p>").append(segments).append("</p>");
    }
}
