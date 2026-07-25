package com.almonium.infra.storage.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class BookHtmlSanitizerTest {
    private final BookHtmlSanitizer sanitizer = new BookHtmlSanitizer();

    @Test
    void removesExecutableBookMarkupAndPreservesReaderStructure() {
        String html =
                """
                <div class="chapter">
                  <h2 id="chapter-1" onclick="alert(1)">Chapter</h2>
                  <script>alert(2)</script>
                  <svg><script>alert(3)</script></svg>
                  <a href="javascript:alert(4)" style="color:red">bad link</a>
                  <img src="data:text/html,bad" onerror="alert(5)" alt="cover">
                  <span class="segment" lang="en"><em>Text</em></span>
                </div>
                """;

        String result = sanitize(html, "books/1.html");

        assertThat(result)
                .contains("<div class=\"chapter\">", "<h2 id=\"chapter-1\">Chapter</h2>")
                .contains("<span class=\"segment\" lang=\"en\"><em>Text</em></span>")
                .doesNotContain("<script", "<svg", "onclick", "onerror", "style=", "javascript:", "data:text/html");
    }

    @Test
    void sanitizesParallelBookPaths() {
        assertThat(sanitize("<p>Safe</p><script>bad()</script>", "books/1-2.html"))
                .isEqualTo("<p>Safe</p>");
    }

    @Test
    void leavesNonBookStorageObjectsUntouched() {
        byte[] content = "<svg onload=\"run()\"></svg>".getBytes(StandardCharsets.UTF_8);

        assertThat(sanitizer.sanitizeIfBookHtml(content, "avatars/user.svg")).isSameAs(content);
    }

    private String sanitize(String html, String path) {
        return new String(
                sanitizer.sanitizeIfBookHtml(html.getBytes(StandardCharsets.UTF_8), path), StandardCharsets.UTF_8);
    }
}
