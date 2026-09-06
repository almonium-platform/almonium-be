package com.almonium.infra.email.util;

import jakarta.validation.constraints.NotNull;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class CssInliner {
    /**
     * A {@code <style data-embed>} block is left alone: it carries the rules that cannot be inlined - {@code :root},
     * media queries, and Outlook's {@code data-ogsb}/{@code data-ogsc} dark-mode hooks, which select elements that do
     * not exist yet at render time.
     */
    private static final String KEEP_MARKER = "data-embed";

    private static final Pattern CSS_COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);

    private CssInliner() {}

    public static String inlineCss(String html) {
        final String style = "style";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select(style + ":not([" + KEEP_MARKER + "])");
        for (Element e : els) {
            String styleRules = CSS_COMMENT
                    .matcher(e.getAllElements().get(0).data())
                    .replaceAll(StringUtils.EMPTY)
                    .replaceAll("\n", StringUtils.EMPTY)
                    .trim();
            String delims = "{}";
            StringTokenizer st = new StringTokenizer(styleRules, delims);
            while (st.countTokens() > 1) {
                String selector = st.nextToken(), properties = st.nextToken();
                if (!selector.contains(":")) { // skip a:hover rules, etc.
                    Elements selectedElements = doc.select(selector);
                    for (Element selElem : selectedElements) {
                        String oldProperties = selElem.attr(style);
                        selElem.attr(
                                style,
                                !oldProperties.isEmpty()
                                        ? concatenateProperties(oldProperties, properties)
                                        : properties);
                    }
                }
            }
            e.remove();
        }
        return doc.toString();
    }

    private static String concatenateProperties(String oldProp, @NotNull String newProp) {
        oldProp = oldProp.trim();
        if (!oldProp.endsWith(";")) oldProp += ";";
        return oldProp + newProp.replaceAll("\\s{2,}", " ");
    }
}
