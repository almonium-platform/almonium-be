package com.almonium.learning.book.service;

import com.almonium.learning.book.dto.response.BookChapter;
import com.almonium.learning.book.dto.response.ChapterVocabulary;
import com.almonium.learning.book.model.entity.Book;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    public List<BookChapter> chaptersFor(Book book) {
        BookChapter[] chapters;
        try {
            chapters = restTemplate.getForObject(
                    processorUrl + "/public/editions/{slug}/chapters/", BookChapter[].class, book.getEditionSlug());
        } catch (HttpClientErrorException.NotFound unavailable) {
            throw new EntityNotFoundException("Published edition chapters are unavailable");
        }
        if (chapters == null) throw new IllegalStateException("Published edition has no chapter response");
        return List.of(chapters);
    }

    public ChapterVocabulary vocabularyFor(Book book, int chapterSequence) {
        if (chapterSequence < 1) throw new EntityNotFoundException("Chapter not found");
        ChapterVocabulary response;
        try {
            response = restTemplate.getForObject(
                    processorUrl + "/public/editions/{slug}/chapters/{sequence}/vocabulary/",
                    ChapterVocabulary.class,
                    book.getEditionSlug(),
                    chapterSequence);
        } catch (HttpClientErrorException.NotFound unavailable) {
            throw new EntityNotFoundException("Published chapter vocabulary is unavailable");
        }
        if (response == null || response.chapterSequence() != chapterSequence || response.words() == null) {
            throw new IllegalStateException("Invalid published chapter vocabulary response");
        }
        return response;
    }

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
        JsonNode payload;
        try {
            payload = restTemplate.getForObject(
                    processorUrl + "/public/editions/{slug}/parallel/{otherSlug}/",
                    JsonNode.class,
                    primary.getEditionSlug(),
                    secondary.getEditionSlug());
        } catch (HttpClientErrorException.NotFound unavailable) {
            throw new EntityNotFoundException("These editions do not have a published, compatible parallel text");
        }
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
                    chapterTitle,
                    block);
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
            String chapterTitle,
            JsonNode block) {
        String segments = "<span class=\"seg-pair\"><span class=\"segment\" data-side=\"primary\" lang=\""
                + HtmlUtils.htmlEscape(primaryLanguage)
                + "\">"
                + sentenceText(block, "primary", primaryText)
                + "</span><span class=\"segment\" data-side=\"secondary\" lang=\""
                + HtmlUtils.htmlEscape(secondaryLanguage)
                + "\">"
                + sentenceText(block, "secondary", secondaryText)
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

    private String sentenceText(JsonNode block, String side, String text) {
        JsonNode sentences = block.path(side + "_sentences");
        JsonNode groups = block.path("sentence_alignment");
        if (!validSentenceMapping(block)) return escapedText(text);
        StringBuilder html = new StringBuilder();
        int cursor = 0;
        int length = text.codePointCount(0, text.length());
        for (int i = 0; i < sentences.size(); i++) {
            int start = sentences.get(i).path("start").asInt(-1);
            int end = sentences.get(i).path("end").asInt(-1);
            if (start < cursor || end <= start || end > length) return escapedText(text);
            html.append(escapedText(codePointSlice(text, cursor, start)));
            int matched = -1;
            for (int g = 0; g < groups.size(); g++) {
                JsonNode group = groups.get(g);
                if (!group.path("certain").asBoolean(false)
                        || group.path("primary").isEmpty()
                        || group.path("secondary").isEmpty()) continue;
                for (JsonNode index : group.path(side)) {
                    if (index.asInt(-1) == i) matched = g;
                }
            }
            if (matched >= 0) {
                html.append("<span class=\"aligned-sentence\" role=\"button\" tabindex=\"0\" data-alignment=\"")
                        .append(block.path("chapter").asInt())
                        .append('-')
                        .append(block.path("sequence").asInt())
                        .append('-')
                        .append(matched)
                        .append("\">");
            }
            html.append(escapedText(codePointSlice(text, start, end)));
            if (matched >= 0) html.append("</span>");
            cursor = end;
        }
        html.append(escapedText(codePointSlice(text, cursor, length)));
        return html.toString();
    }

    private String codePointSlice(String text, int start, int end) {
        return text.substring(text.offsetByCodePoints(0, start), text.offsetByCodePoints(0, end));
    }

    /** Never render a clickable sentence whose opposite side cannot carry its counterpart. */
    private boolean validSentenceMapping(JsonNode block) {
        JsonNode groups = block.path("sentence_alignment");
        if (!groups.isArray() || groups.isEmpty()) return false;
        for (String side : List.of("primary", "secondary")) {
            JsonNode sentences = block.path(side + "_sentences");
            if (!sentences.isArray() || sentences.isEmpty()) return false;
            String text = block.path(side + "_text").asString();
            int length = text.codePointCount(0, text.length());
            int cursor = 0;
            for (JsonNode sentence : sentences) {
                if (!sentence.path("start").isInt() || !sentence.path("end").isInt()) return false;
                int start = sentence.path("start").asInt();
                int end = sentence.path("end").asInt();
                if (start < cursor || end <= start || end > length) return false;
                cursor = end;
            }
            boolean[] used = new boolean[sentences.size()];
            for (JsonNode group : groups) {
                if (!group.path("certain").isBoolean()
                        || !group.path("primary").isArray()
                        || !group.path("secondary").isArray()
                        || (group.path("primary").isEmpty()
                                && group.path("secondary").isEmpty())) return false;
                for (JsonNode index : group.path(side)) {
                    if (!index.isInt()) return false;
                    int value = index.asInt();
                    if (value < 0 || value >= used.length || used[value]) return false;
                    used[value] = true;
                }
            }
            for (boolean covered : used) if (!covered) return false;
        }
        return true;
    }
}
