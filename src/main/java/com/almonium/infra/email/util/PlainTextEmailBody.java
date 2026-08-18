package com.almonium.infra.email.util;

import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.jsoup.Jsoup;

@UtilityClass
public class PlainTextEmailBody {

    public String fromHtml(String html) {
        var document = Jsoup.parse(html);
        String text = document.body()
                .wholeText()
                .replaceAll("[ \\t]+", " ")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();
        String links = document.select("a[href]").stream()
                .map(link -> link.text() + ": " + withoutHttpsScheme(link.attr("href")))
                .collect(Collectors.joining("\n"));

        return links.isBlank() ? text : text + "\n\nLinks:\n" + links;
    }

    public String withoutHttpsScheme(String url) {
        return url.replaceFirst("^https://", "");
    }
}
