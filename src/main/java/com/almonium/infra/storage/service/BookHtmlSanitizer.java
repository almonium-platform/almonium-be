package com.almonium.infra.storage.service;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class BookHtmlSanitizer {
    private static final Pattern BOOK_HTML_PATH = Pattern.compile("^books/[^/]+\\.html$");

    private static final Safelist BOOK_HTML_SAFELIST = Safelist.none()
            .addTags(
                    "a",
                    "b",
                    "blockquote",
                    "br",
                    "cite",
                    "code",
                    "div",
                    "em",
                    "h1",
                    "h2",
                    "h3",
                    "h4",
                    "h5",
                    "h6",
                    "hr",
                    "i",
                    "img",
                    "li",
                    "ol",
                    "p",
                    "pre",
                    "q",
                    "small",
                    "span",
                    "strong",
                    "sub",
                    "sup",
                    "u",
                    "ul")
            .addAttributes(":all", "alt", "class", "dir", "height", "id", "lang", "role", "tabindex", "title", "width")
            .addAttributes("a", "href")
            .addAttributes("img", "src")
            .addProtocols("a", "href", "http", "https", "mailto")
            .addProtocols("img", "src", "http", "https")
            .preserveRelativeLinks(true);

    private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    public byte[] sanitizeIfBookHtml(byte[] content, String filePath) {
        if (!BOOK_HTML_PATH.matcher(filePath).matches()) {
            return content;
        }

        String sanitized =
                Jsoup.clean(new String(content, StandardCharsets.UTF_8), "", BOOK_HTML_SAFELIST, OUTPUT_SETTINGS);
        return sanitized.getBytes(StandardCharsets.UTF_8);
    }
}
