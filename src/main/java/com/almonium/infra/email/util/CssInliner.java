package com.almonium.infra.email.util;

import jakarta.validation.constraints.NotNull;
import java.util.StringTokenizer;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@UtilityClass
public class CssInliner {

    public String inlineCss(String html) {
        final String style = "style";
        Document doc = Jsoup.parse(html);
        Elements els = doc.select(style); // to get all the style elements
        for (Element e : els) {
            String styleRules = e.getAllElements()
                    .get(0)
                    .data()
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

    private String concatenateProperties(String oldProp, @NotNull String newProp) {
        oldProp = oldProp.trim();
        if (!oldProp.endsWith(";")) oldProp += ";";
        return oldProp + newProp.replaceAll("\\s{2,}", " ");
    }
}
