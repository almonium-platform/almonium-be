package com.almonium.learning.catalog.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads the tab-separated results a SPARQL endpoint returns: IRIs in angle brackets, strings quoted with an optional
 * language tag or datatype, numbers bare. Only what the catalogue queries produce is supported.
 */
final class SparqlTsv {
    private static final String ENTITY = "http://www.wikidata.org/entity/";

    private SparqlTsv() {}

    static List<String> cells(String line) {
        List<String> cells = new ArrayList<>();
        int start = 0;
        for (int i = 0; i <= line.length(); i++) {
            if (i == line.length() || line.charAt(i) == '\t') {
                cells.add(value(line.substring(start, i)));
                start = i + 1;
            }
        }
        return cells;
    }

    /** The plain value of one cell: the Q-id of an entity, the text of a literal, empty for an unbound cell. */
    static String value(String cell) {
        if (cell.isEmpty()) {
            return "";
        }
        if (cell.startsWith("<") && cell.endsWith(">")) {
            String iri = cell.substring(1, cell.length() - 1);
            return iri.startsWith(ENTITY) ? iri.substring(ENTITY.length()) : iri;
        }
        if (cell.startsWith("\"")) {
            int end = cell.lastIndexOf('"');
            return end <= 0 ? cell.substring(1) : unescape(cell.substring(1, end));
        }
        return cell;
    }

    /** The language tag of a literal cell ("en" for {@code "Dracula"@en}), empty when it has none. */
    static String language(String cell) {
        int end = cell.lastIndexOf('"');
        return end > 0 && end + 1 < cell.length() && cell.charAt(end + 1) == '@' ? cell.substring(end + 2) : "";
    }

    static List<String> raw(String line) {
        List<String> cells = new ArrayList<>();
        int start = 0;
        for (int i = 0; i <= line.length(); i++) {
            if (i == line.length() || line.charAt(i) == '\t') {
                cells.add(line.substring(start, i));
                start = i + 1;
            }
        }
        return cells;
    }

    static Integer integer(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static String unescape(String text) {
        if (text.indexOf('\\') < 0) {
            return text;
        }
        StringBuilder out = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\\' && i + 1 < text.length()) {
                char next = text.charAt(++i);
                switch (next) {
                    case 'n' -> out.append(' ');
                    case 't' -> out.append(' ');
                    case 'r' -> out.append(' ');
                    default -> out.append(next);
                }
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
