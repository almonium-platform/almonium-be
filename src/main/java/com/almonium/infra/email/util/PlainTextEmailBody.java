package com.almonium.infra.email.util;

import java.util.stream.Collectors;
import org.jsoup.Jsoup;

public final class PlainTextEmailBody {
    private PlainTextEmailBody() {}

    /** The hidden inbox-preview block; see {@code _fragments/preheader.html}. */
    private static final String PREHEADER_SELECTOR = ".preheader";

    private static final String ZERO_WIDTH_SPACE = "\u200B";

    public static String fromHtml(String html) {
        var document = Jsoup.parse(html);
        // The preheader exists for the HTML preview only; a text-only reader would open on a wall of blank characters.
        document.select(PREHEADER_SELECTOR).remove();
        String text = document.body()
                .wholeText()
                .replace(ZERO_WIDTH_SPACE, "")
                .replaceAll("[ \\t]+", " ")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();
        String links = document.select("a[href]").stream()
                .map(link -> link.text() + ": " + withoutHttpsScheme(link.attr("href")))
                .collect(Collectors.joining("\n"));

        return links.isBlank() ? text : text + "\n\nLinks:\n" + links;
    }

    public static String withoutHttpsScheme(String url) {
        return url.replaceFirst("^https://", "");
    }
}
